package com.softwaremagico.tm.file.configurator;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.softwaremagico.tm.exceptions.PropertyNotFoundException;
import com.softwaremagico.tm.exceptions.PropertyNotStoredException;
import com.softwaremagico.tm.log.ConfigurationLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public abstract class ConfigurationReader {
	private static final String VALUES_SEPARATOR_REGEX = " *, *";
	private static final char PREFIX_SEPARATOR_CHAR = '.';
	private final Map<Class<?>, IValueConverter<?>> converter;
	private final Map<String, String> propertiesDefault;
	private final Map<String, String> propertiesFinalValue;
	private final List<IPropertiesSource> propertiesSources;
	private final Set<PropertyChangedListener> propertyChangedListeners;
	private final Map<IPropertiesSource, Map<String, String>> propertiesBySourceValues;

	protected ConfigurationReader() {
		this.converter = new HashMap<>();
		this.propertiesDefault = new HashMap<>();
		this.propertiesFinalValue = new HashMap<>();
		this.propertiesSources = new ArrayList<>();
		this.propertyChangedListeners = new HashSet<>();
		this.propertiesBySourceValues = new HashMap<>();

		this.addConverter(Boolean.class, new BooleanValueConverter());
		this.addConverter(Integer.class, new IntegerValueConverter());
		this.addConverter(Double.class, new DoubleValueConverter());

		// Log if any property has changed the value.
		this.addPropertyChangedListener(new PropertyChangedListener() {

			@Override
			public void propertyChanged(String propertyId, String oldValue, String newValue) {
				ConfigurationLog.info(this.getClass().getName(), "Property '" + propertyId
						+ "' has changed value from '" + oldValue + "' to '" + newValue + "'.");
			}
		});
	}

	public interface PropertyChangedListener {
		void propertyChanged(String propertyId, String oldValue, String newValue);
	}

	public <T> void addConverter(Class<T> clazz, IValueConverter<T> valueConverter) {
		this.converter.put(clazz, valueConverter);
	}

	@SuppressWarnings("unchecked")
	public <T> IValueConverter<T> getConverter(Class<T> clazz) {
		return (IValueConverter<T>) this.converter.get(clazz);
	}

	public void addPropertiesSource(IPropertiesSource propertiesSource) {
		this.propertiesSources.add(propertiesSource);
	}

	public void removePropertiesSource(IPropertiesSource propertiesSource) {
		if (propertiesSource != null) {
			this.propertiesSources.remove(propertiesSource);
		}
	}

	/**
	 * Restarts all properties to their default values and then reads all the
	 * configuration files again.
	 */
	public void readConfigurations() {
		this.propertiesFinalValue.clear();
		this.propertiesFinalValue.putAll(this.propertiesDefault);

		for (final IPropertiesSource propertiesSource : this.propertiesSources) {
			final Properties propertyFile = propertiesSource.loadFile();
			if (propertyFile != null) {
				this.readAllProperties(propertyFile, propertiesSource);
			}
		}
	}

	public abstract void storeProperties() throws PropertyNotStoredException;

	public abstract File getUserProperties();

	/**
	 * Reads all properties configured in this configuration reader from
	 * propertyFile. If they doesn't exist, then the current value is mantained as
	 * default value.
	 *
	 * @param propertyFile
	 */
	private void readAllProperties(Properties propertyFile, IPropertiesSource propertiesSource) {
		for (final String propertyId : new HashSet<String>(this.propertiesFinalValue.keySet())) {
			final String value = propertyFile.getProperty(propertyId, this.propertiesFinalValue.get(propertyId));
			// Notify property change
			this.propertiesBySourceValues.computeIfAbsent(propertiesSource, k -> new HashMap<String, String>());

			if (this.propertiesBySourceValues.get(propertiesSource).get(propertyId) != null
					&& this.propertiesBySourceValues.get(propertiesSource).get(propertyId).length() > 0
					&& !this.propertiesBySourceValues.get(propertiesSource).get(propertyId).equals(value)) {
				// Launch listeners.
				for (final PropertyChangedListener propertyChangedListener : this.propertyChangedListeners) {
					propertyChangedListener.propertyChanged(propertyId, this.propertiesFinalValue.get(propertyId),
							value);
				}
				ConfigurationLog.info(this.getClass().getName(),
						"Property '" + propertyId + "' updated as '" + value + "'.");
			}
			// Store value.
			this.propertiesBySourceValues.get(propertiesSource).put(propertyId, value);
			this.propertiesFinalValue.put(propertyId, value);
			ConfigurationLog.debug(this.getClass().getName(),
					"Property '" + propertyId + "' set as value '" + value + "'.");
		}
	}

	/**
	 * Adds a property
	 *
	 * @param propertyName
	 * @param defaultValue
	 */
	protected <T> void setProperty(String propertyName, T defaultValue) {
		if (defaultValue == null) {
			this.propertiesDefault.put(propertyName, null);
			this.propertiesFinalValue.put(propertyName, null);
		} else if (defaultValue instanceof String) {
			this.propertiesDefault.put(propertyName, ((String) defaultValue).trim());
			this.propertiesFinalValue.put(propertyName, ((String) defaultValue).trim());
		} else {
			this.propertiesDefault.put(propertyName,
					this.getConverter(defaultValue.getClass()).convertToString(defaultValue));
			this.propertiesFinalValue.put(propertyName,
					this.getConverter(defaultValue.getClass()).convertToString(defaultValue));
		}
	}

	/**
	 * Read all properties and set an empty string as default value.
	 */
	public void initializeAllProperties() {
		for (final IPropertiesSource propertiesSource : this.propertiesSources) {
			final Properties propertyFile = propertiesSource.loadFile();
			if (propertyFile != null) {
				final Enumeration<?> enumerator = propertyFile.propertyNames();
				while (enumerator.hasMoreElements()) {
					this.setProperty((String) enumerator.nextElement(), "");
				}
			}
		}
	}

	/**
	 * Gets all defined prefix for the properties.
	 *
	 * @return
	 */
	public Set<String> getAllPropertiesPrefixes() {
		final Set<String> prefixes = new HashSet<>();
		for (final IPropertiesSource propertiesSource : this.propertiesSources) {
			final Properties propertyFile = propertiesSource.loadFile();
			if (propertyFile != null) {
				final Enumeration<?> enumerator = propertyFile.propertyNames();
				while (enumerator.hasMoreElements()) {
					final String element = (String) enumerator.nextElement();
					if (element.contains(String.valueOf(PREFIX_SEPARATOR_CHAR))) {
						prefixes.add(element.substring(0, element.indexOf(PREFIX_SEPARATOR_CHAR)));
					}
				}
			}
		}
		return prefixes;
	}

	public String getProperty(String propertyName) throws PropertyNotFoundException {
		if (this.propertiesFinalValue.containsKey(propertyName)) {
			if (this.propertiesFinalValue.get(propertyName) != null) {
				return this.propertiesFinalValue.get(propertyName).trim();
			} else {
				return null;
			}
		} else {
			throw new PropertyNotFoundException(
					"Property '" + propertyName + "' not defined in the configuration reader");
		}
	}

	public <T> T getProperty(String propertyName, Class<? extends T> type) throws PropertyNotFoundException {
		final String stringValue = this.getProperty(propertyName);
		if (stringValue != null) {
			return this.getConverter(type).convertFromString(stringValue);
		} else {
			return null;
		}
	}

	public Map<String, String> getAllProperties() {
		return this.propertiesFinalValue;
	}

	public List<IPropertiesSource> getPropertiesSources() {
		return this.propertiesSources;
	}

	public void addPropertyChangedListener(PropertyChangedListener propertyChangedListener) {
		this.propertyChangedListeners.add(propertyChangedListener);
	}

	/**
	 * Stops file watchers in from all configuration files.
	 */
	public void stopFileWatchers() {
		for (final IPropertiesSource sources : this.propertiesSources) {
			if (sources instanceof PropertiesSourceFile) {
				((PropertiesSourceFile) sources).stopFileWatcher();
			}
		}
	}

	protected String[] getCommaSeparatedValues(String propertyName) throws PropertyNotFoundException {
		String value = this.getProperty(propertyName);
		// Remove useless spaces around commas.
		value = value.replaceAll(VALUES_SEPARATOR_REGEX, ",");
		// Split by commas.
		return value.split(",");

	}

	public abstract String getUserPropertiesPath();

}
