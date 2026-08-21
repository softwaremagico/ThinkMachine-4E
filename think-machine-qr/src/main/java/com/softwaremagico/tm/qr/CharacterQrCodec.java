package com.softwaremagico.tm.qr;

/*-
 * #%L
 * Think Machine 4E (QR)
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.CharacterSelectedElement;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicReassign;
import com.softwaremagico.tm.character.equipment.CharacterSelectedEquipment;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.perks.AfflictionFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.skills.SkillsReassign;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Encodes a {@link CharacterPlayer} to a compact URL-safe Base64 string (GZIP-compressed JSON)
 * suitable for embedding in a QR code, and decodes it back.
 *
 * <p>The codec only stores IDs (never positional indices), so adding new game elements
 * to any module will not invalidate existing QR codes.
 *
 * <p>Free-text description fields are intentionally excluded from the payload to keep
 * the payload within QR-code size limits.
 */
public final class CharacterQrCodec {

    private static final String SPECIALIZATION_SEPARATOR = ":";

    private static ObjectMapper objectMapper;

    private CharacterQrCodec() {
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Encodes the character to a URL-safe Base64 string.
     * The string can be passed directly to a QR writer.
     */
    public static String encode(CharacterPlayer player) throws IOException {
        final CharacterQrData data = toData(player);
        final String json = getMapper().writeValueAsString(data);
        final byte[] compressed = gzip(json.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(compressed);
    }

    /**
     * Decodes a URL-safe Base64 string (as produced by {@link #encode}) back into a
     * {@link CharacterPlayer}.
     */
    public static CharacterPlayer decode(String payload) throws IOException {
        final byte[] compressed = Base64.getUrlDecoder().decode(payload);
        final String json = new String(gunzip(compressed), StandardCharsets.UTF_8);
        final CharacterQrData data = getMapper().readValue(json, CharacterQrData.class);
        return fromData(data);
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private static ObjectMapper getMapper() {
        if (objectMapper == null) {
            objectMapper = JsonMapper.builder()
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .build();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        return objectMapper;
    }

    // ── Encoding ──────────────────────────────────────────────────────────────

    private static CharacterQrData toData(CharacterPlayer player) {
        final CharacterQrData data = new CharacterQrData();

        encodeInfo(player, data);
        encodeCore(player, data);
        encodeStepSelections(player, data);
        encodeLevels(player, data);
        encodeOccultism(player, data);
        encodeEquipment(player, data);
        encodeReassigns(player, data);
        encodeAffliction(player, data);

        return data;
    }

    private static void encodeInfo(CharacterPlayer player, CharacterQrData data) {
        if (player.getInfo() == null) {
            return;
        }
        data.setName(player.getInfo().getNameRepresentation().isEmpty() ? null : player.getInfo().getNameRepresentation());
        data.setSurname(player.getInfo().getSurname() != null ? player.getInfo().getSurname().getNameRepresentation() : null);
        data.setPlayer(player.getInfo().getPlayer());
        data.setGender(player.getInfo().getGender() != null ? player.getInfo().getGender().name() : null);
        data.setAge(player.getInfo().getAge());
        data.setPlanet(player.getInfo().getPlanet());
        data.setHair(player.getInfo().getHair());
        data.setEyes(player.getInfo().getEyes());
        data.setComplexion(player.getInfo().getComplexion());
        data.setHeight(player.getInfo().getHeight());
        data.setWeight(player.getInfo().getWeight());
    }

    private static void encodeCore(CharacterPlayer player, CharacterQrData data) {
        data.setPrimaryCharacteristic(player.getPrimaryCharacteristic());
        data.setSecondaryCharacteristic(player.getSecondaryCharacteristic());
        data.setSpecie(player.getSpecie() != null ? player.getSpecie().getId() : null);
        data.setUpbringing(player.getUpbringing() != null ? player.getUpbringing().getId() : null);
        data.setFaction(player.getFaction() != null ? player.getFaction().getId() : null);
        data.setCalling(player.getCalling() != null ? player.getCalling().getId() : null);
    }

    private static void encodeStepSelections(CharacterPlayer player, CharacterQrData data) {
        if (player.getSpecie() != null) {
            data.setSpecieSelections(extractStepSelections(player.getSpecie()));
        }
        if (player.getUpbringing() != null) {
            data.setUpbringingSelections(extractStepSelections(player.getUpbringing()));
        }
        if (player.getFaction() != null) {
            data.setFactionSelections(extractStepSelections(player.getFaction()));
        }
        if (player.getCalling() != null) {
            data.setCallingSelections(extractStepSelections(player.getCalling()));
        }
    }

    private static void encodeLevels(CharacterPlayer player, CharacterQrData data) {
        if (player.getLevels().isEmpty()) {
            return;
        }
        final List<LevelSelectionData> levels = new ArrayList<>();
        for (final LevelSelector level : player.getLevels()) {
            final LevelSelectionData ld = new LevelSelectionData();
            ld.setCallingId(level.getCallingId());
            ld.setCapabilities(extractOptionSlots(level.getSelectedCapabilityOptions()));
            ld.setCharacteristics(extractOptionSlots(level.getSelectedCharacteristicOptions()));
            ld.setSkills(extractOptionSlots(level.getSelectedSkillOptions()));
            ld.setClassPerks(extractOptionSlots(level.getSelectedClassPerksOptions()));
            ld.setCallingPerks(extractOptionSlots(level.getSelectedCallingPerksOptions()));
            // Levels don't have perks or material awards in the same slots
            levels.add(ld);
        }
        data.setLevels(levels);
    }

    private static void encodeOccultism(CharacterPlayer player, CharacterQrData data) {
        // Dark side values
        final Map<String, Integer> darkSide = new HashMap<>();
        try {
            for (final OccultismType type : OccultismTypeFactory.getInstance().getElements()) {
                final int value = player.getDarkSideLevel(type);
                if (value > 0) {
                    darkSide.put(type.getId(), value);
                }
            }
        } catch (final InvalidXmlElementException ignored) {
            // If factory unavailable, skip
        }
        if (!darkSide.isEmpty()) {
            data.setDarkSide(darkSide);
        }

        // Selected powers
        final Map<String, List<OccultismPower>> selectedPowers = player.getSelectedPowers();
        if (!selectedPowers.isEmpty()) {
            final Map<String, List<String>> powers = new HashMap<>();
            selectedPowers.forEach((pathId, powerList) -> {
                final List<String> powerIds = powerList.stream().map(OccultismPower::getId).toList();
                if (!powerIds.isEmpty()) {
                    powers.put(pathId, powerIds);
                }
            });
            if (!powers.isEmpty()) {
                data.setPowers(powers);
            }
        }
    }

    private static void encodeEquipment(CharacterPlayer player, CharacterQrData data) {
        final List<String> ids = player.getEquipmentPurchased().stream()
                .map(Element::getId)
                .filter(Objects::nonNull)
                .toList();
        if (!ids.isEmpty()) {
            data.setEquipmentPurchased(ids);
        }
    }

    private static void encodeReassigns(CharacterPlayer player, CharacterQrData data) {
        if (!player.getCharacteristicReassigns().isEmpty()) {
            final List<String[]> list = player.getCharacteristicReassigns().stream()
                    .map(r -> new String[]{r.getFrom(), r.getTo()})
                    .toList();
            data.setCharacteristicReassigns(list);
        }
        if (!player.getSkillsReassigns().isEmpty()) {
            final List<String[]> list = player.getSkillsReassigns().stream()
                    .map(r -> new String[]{r.getFrom(), r.getTo()})
                    .toList();
            data.setSkillsReassigns(list);
        }
    }

    private static void encodeAffliction(CharacterPlayer player, CharacterQrData data) {
        if (player.getAffliction() != null) {
            data.setAffliction(player.getAffliction().getId());
        }
    }

    // ── Step extraction helpers ───────────────────────────────────────────────

    private static StepSelectionData extractStepSelections(CharacterDefinitionStepSelection step) {
        final StepSelectionData sd = new StepSelectionData();
        sd.setCapabilities(extractOptionSlots(step.getSelectedCapabilityOptions()));
        sd.setCharacteristics(extractOptionSlots(step.getSelectedCharacteristicOptions()));
        sd.setSkills(extractOptionSlots(step.getSelectedSkillOptions()));
        sd.setPerks(extractOptionSlots(step.getSelectedPerksOptions()));
        sd.setMaterialAwards(extractEquipmentSlots(step.getSelectedMaterialAwards()));
        return sd;
    }

    private static List<List<String>> extractOptionSlots(List<CharacterSelectedElement> slots) {
        if (slots == null || slots.isEmpty()) {
            return null;
        }
        boolean hasContent = false;
        final List<List<String>> result = new ArrayList<>();
        for (final CharacterSelectedElement slot : slots) {
            final List<String> encoded = slot.getSelections().stream()
                    .filter(s -> s.getId() != null)
                    .map(CharacterQrCodec::encodeSelection)
                    .toList();
            result.add(encoded.isEmpty() ? null : encoded);
            if (!encoded.isEmpty()) {
                hasContent = true;
            }
        }
        return hasContent ? result : null;
    }

    private static List<EquipmentSlotData> extractEquipmentSlots(List<CharacterSelectedEquipment> slots) {
        if (slots == null || slots.isEmpty()) {
            return null;
        }
        boolean hasContent = false;
        final List<EquipmentSlotData> result = new ArrayList<>();
        for (final CharacterSelectedEquipment slot : slots) {
            final List<String> selected = slot.getSelections().stream()
                    .filter(s -> s.getId() != null)
                    .map(Selection::getId)
                    .toList();
            final List<String> removed = slot.getRemoved().stream()
                    .filter(s -> s.getId() != null)
                    .map(Selection::getId)
                    .toList();
            if (!selected.isEmpty() || !removed.isEmpty()) {
                final EquipmentSlotData esd = new EquipmentSlotData();
                esd.setSelected(selected.isEmpty() ? null : selected);
                esd.setRemoved(removed.isEmpty() ? null : removed);
                result.add(esd);
                hasContent = true;
            } else {
                result.add(null);
            }
        }
        return hasContent ? result : null;
    }

    private static String encodeSelection(Selection s) {
        if (s.getSpecialization() != null) {
            return s.getId() + SPECIALIZATION_SEPARATOR + s.getSpecialization().getId();
        }
        return s.getId();
    }

    // ── Decoding ──────────────────────────────────────────────────────────────

    private static CharacterPlayer fromData(CharacterQrData data) {
        final CharacterPlayer player = new CharacterPlayer();

        decodeInfo(data, player);
        decodeCore(data, player);
        decodeStepSelections(data, player);
        decodeLevels(data, player);
        decodeOccultism(data, player);
        decodeEquipment(data, player);
        decodeReassigns(data, player);
        decodeAffliction(data, player);

        return player;
    }

    private static void decodeInfo(CharacterQrData data, CharacterPlayer player) {
        if (data.getName() != null) {
            player.getInfo().setNames(data.getName());
        }
        if (data.getSurname() != null) {
            player.getInfo().setSurname(data.getSurname());
        }
        player.getInfo().setPlayer(data.getPlayer());
        if (data.getGender() != null) {
            try {
                player.getInfo().setGender(Gender.valueOf(data.getGender()));
            } catch (final IllegalArgumentException ignored) {
                // unknown gender value - skip
            }
        }
        player.getInfo().setAge(data.getAge());
        player.getInfo().setPlanet(data.getPlanet());
        player.getInfo().setHair(data.getHair());
        player.getInfo().setEyes(data.getEyes());
        player.getInfo().setComplexion(data.getComplexion());
        player.getInfo().setHeight(data.getHeight());
        player.getInfo().setWeight(data.getWeight());
    }

    private static void decodeCore(CharacterQrData data, CharacterPlayer player) {
        if (data.getSpecie() != null) {
            player.setSpecie(data.getSpecie());
        }
        if (data.getUpbringing() != null) {
            player.setUpbringing(data.getUpbringing());
        }
        if (data.getFaction() != null) {
            player.setFaction(data.getFaction());
        }
        if (data.getCalling() != null) {
            player.setCalling(data.getCalling());
        }
        player.setPrimaryCharacteristic(data.getPrimaryCharacteristic());
        player.setSecondaryCharacteristic(data.getSecondaryCharacteristic());
    }

    private static void decodeStepSelections(CharacterQrData data, CharacterPlayer player) {
        if (data.getSpecieSelections() != null && player.getSpecie() != null) {
            applyStepSelections(player.getSpecie(), data.getSpecieSelections());
        }
        if (data.getUpbringingSelections() != null && player.getUpbringing() != null) {
            applyStepSelections(player.getUpbringing(), data.getUpbringingSelections());
        }
        if (data.getFactionSelections() != null && player.getFaction() != null) {
            applyStepSelections(player.getFaction(), data.getFactionSelections());
        }
        if (data.getCallingSelections() != null && player.getCalling() != null) {
            applyStepSelections(player.getCalling(), data.getCallingSelections());
        }
    }

    private static void decodeLevels(CharacterQrData data, CharacterPlayer player) {
        if (data.getLevels() == null) {
            return;
        }
        for (final LevelSelectionData ld : data.getLevels()) {
            final LevelSelector level = player.addLevel();
            if (ld.getCallingId() != null) {
                level.setCalling(ld.getCallingId());
            }
            applyOptionSlots(level.getSelectedCapabilityOptions(), ld.getCapabilities(),
                    CharacterQrCodec::lookupCapability);
            applyOptionSlots(level.getSelectedCharacteristicOptions(), ld.getCharacteristics(),
                    CharacterQrCodec::lookupCharacteristic);
            applyOptionSlots(level.getSelectedSkillOptions(), ld.getSkills(),
                    CharacterQrCodec::lookupSkill);
            applyOptionSlots(level.getSelectedClassPerksOptions(), ld.getClassPerks(),
                    CharacterQrCodec::lookupPerk);
            applyOptionSlots(level.getSelectedCallingPerksOptions(), ld.getCallingPerks(),
                    CharacterQrCodec::lookupPerk);
        }
    }

    private static void decodeOccultism(CharacterQrData data, CharacterPlayer player) {
        // Dark side
        if (data.getDarkSide() != null) {
            data.getDarkSide().forEach((typeId, value) -> {
                try {
                    final OccultismType type = OccultismTypeFactory.getInstance().getElement(typeId);
                    player.setDarkSideLevel(type, value);
                } catch (final InvalidXmlElementException ignored) {
                    // unknown occultism type - skip
                }
            });
        }
        // Powers
        if (data.getPowers() != null) {
            data.getPowers().forEach((pathId, powerIds) -> {
                try {
                    final OccultismPath path = OccultismPathFactory.getInstance().getElement(pathId);
                    for (final String powerId : powerIds) {
                        try {
                            final OccultismPower power = path.getOccultismPowers().get(powerId);
                            if (power != null) {
                                player.addOccultismPower(power);
                            }
                        } catch (final Exception ignored) {
                            // power not available - skip
                        }
                    }
                } catch (final InvalidXmlElementException ignored) {
                    // unknown path - skip
                }
            });
        }
    }

    private static void decodeEquipment(CharacterQrData data, CharacterPlayer player) {
        if (data.getEquipmentPurchased() == null) {
            return;
        }
        for (final String id : data.getEquipmentPurchased()) {
            final Equipment equipment = resolveEquipment(id);
            if (equipment != null) {
                player.addEquipmentPurchased(equipment);
            }
        }
    }

    private static void decodeReassigns(CharacterQrData data, CharacterPlayer player) {
        if (data.getCharacteristicReassigns() != null) {
            data.getCharacteristicReassigns().forEach(pair -> {
                if (pair != null && pair.length == 2) {
                    player.getCharacteristicReassigns().add(new CharacteristicReassign(pair[0], pair[1]));
                }
            });
        }
        if (data.getSkillsReassigns() != null) {
            data.getSkillsReassigns().forEach(pair -> {
                if (pair != null && pair.length == 2) {
                    player.getSkillsReassigns().add(new SkillsReassign(pair[0], pair[1]));
                }
            });
        }
    }

    private static void decodeAffliction(CharacterQrData data, CharacterPlayer player) {
        if (data.getAffliction() != null) {
            try {
                player.setAffliction(AfflictionFactory.getInstance().getElement(data.getAffliction()));
            } catch (final InvalidXmlElementException ignored) {
                // unknown affliction - skip
            }
        }
    }

    // ── Step application helpers ──────────────────────────────────────────────

    private static void applyStepSelections(CharacterDefinitionStepSelection step, StepSelectionData sd) {
        applyOptionSlots(step.getSelectedCapabilityOptions(), sd.getCapabilities(),
                CharacterQrCodec::lookupCapability);
        applyOptionSlots(step.getSelectedCharacteristicOptions(), sd.getCharacteristics(),
                CharacterQrCodec::lookupCharacteristic);
        applyOptionSlots(step.getSelectedSkillOptions(), sd.getSkills(),
                CharacterQrCodec::lookupSkill);
        applyOptionSlots(step.getSelectedPerksOptions(), sd.getPerks(),
                CharacterQrCodec::lookupPerk);
        applyEquipmentSlots(step.getSelectedMaterialAwards(), sd.getMaterialAwards());
    }

    @FunctionalInterface
    private interface ElementLookup {
        Element lookup(String id) throws InvalidXmlElementException;
    }

    private static void applyOptionSlots(List<CharacterSelectedElement> slots,
                                         List<List<String>> encoded,
                                         ElementLookup lookup) {
        if (encoded == null || slots == null) {
            return;
        }
        for (int i = 0; i < Math.min(slots.size(), encoded.size()); i++) {
            final List<String> slotEncoded = encoded.get(i);
            if (slotEncoded == null || slotEncoded.isEmpty()) {
                continue;
            }
            final List<Selection> selections = new ArrayList<>();
            for (final String encoded1 : slotEncoded) {
                final Selection selection = decodeSelection(encoded1, lookup);
                if (selection != null) {
                    selections.add(selection);
                }
            }
            if (!selections.isEmpty()) {
                slots.get(i).setSelections(selections);
            }
        }
    }

    private static void applyEquipmentSlots(List<CharacterSelectedEquipment> slots,
                                             List<EquipmentSlotData> encoded) {
        if (encoded == null || slots == null) {
            return;
        }
        for (int i = 0; i < Math.min(slots.size(), encoded.size()); i++) {
            final EquipmentSlotData slotData = encoded.get(i);
            if (slotData == null) {
                continue;
            }
            if (slotData.getSelected() != null) {
                final List<Selection> selections = new ArrayList<>();
                for (final String id : slotData.getSelected()) {
                    final Equipment equipment = resolveEquipment(id);
                    if (equipment != null) {
                        selections.add(new Selection(equipment));
                    }
                }
                if (!selections.isEmpty()) {
                    slots.get(i).setSelections(selections);
                }
            }
            if (slotData.getRemoved() != null) {
                for (final String id : slotData.getRemoved()) {
                    final Equipment equipment = resolveEquipment(id);
                    if (equipment != null) {
                        slots.get(i).addRemoved(new Selection(equipment));
                    }
                }
            }
        }
    }

    private static Selection decodeSelection(String encoded, ElementLookup lookup) {
        if (encoded == null || encoded.isBlank()) {
            return null;
        }
        final int sepIdx = encoded.indexOf(SPECIALIZATION_SEPARATOR);
        try {
            if (sepIdx < 0) {
                final Element element = lookup.lookup(encoded);
                return new Selection(element);
            } else {
                final String id = encoded.substring(0, sepIdx);
                final String specializationId = encoded.substring(sepIdx + 1);
                final Element element = lookup.lookup(id);
                return new Selection(element, new Specialization(specializationId));
            }
        } catch (final InvalidXmlElementException ignored) {
            // element no longer exists in the current module set - skip gracefully
            return null;
        }
    }

    // ── Element lookups ───────────────────────────────────────────────────────

    private static Element lookupCapability(String id) throws InvalidXmlElementException {
        return CapabilityFactory.getInstance().getElement(id);
    }

    private static Element lookupCharacteristic(String id) throws InvalidXmlElementException {
        final CharacteristicDefinition def = CharacteristicsDefinitionFactory.getInstance().getElement(id);
        if (def == null) {
            throw new InvalidXmlElementException("Characteristic '" + id + "' not found.");
        }
        return def;
    }

    private static Element lookupSkill(String id) throws InvalidXmlElementException {
        return SkillFactory.getInstance().getElement(id);
    }

    private static Element lookupPerk(String id) throws InvalidXmlElementException {
        return PerkFactory.getInstance().getElement(id);
    }

    /**
     * Resolves an equipment ID by trying all equipment factories in order.
     * Returns {@code null} if the equipment is not found in any factory (module may be disabled).
     */
    static Equipment resolveEquipment(String id) {
        if (id == null) {
            return null;
        }
        try {
            return WeaponFactory.getInstance().getElement(id);
        } catch (final InvalidXmlElementException ignored) { /* not a weapon */ }
        try {
            return ArmorFactory.getInstance().getElement(id);
        } catch (final InvalidXmlElementException ignored) { /* not an armor */ }
        try {
            return ShieldFactory.getInstance().getElement(id);
        } catch (final InvalidXmlElementException ignored) { /* not a shield */ }
        try {
            return HandheldShieldFactory.getInstance().getElement(id);
        } catch (final InvalidXmlElementException ignored) { /* not a handheld shield */ }
        try {
            return ItemFactory.getInstance().getElement(id);
        } catch (final InvalidXmlElementException ignored) { /* not an item */ }
        return null;
    }

    /**
     * Resolves a planet ID via {@link PlanetFactory}, returning null if not found.
     */
    static String resolvePlanetId(String planetId) {
        if (planetId == null) {
            return null;
        }
        try {
            PlanetFactory.getInstance().getElement(planetId);
            return planetId;
        } catch (final InvalidXmlElementException ignored) {
            return null;
        }
    }

    private static final int BUFFER_SIZE = 4096;

    // ── Compression ───────────────────────────────────────────────────────────

    private static byte[] gzip(byte[] data) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gos = new GZIPOutputStream(bos)) {
            gos.write(data);
        }
        return bos.toByteArray();
    }

    private static byte[] gunzip(byte[] compressed) throws IOException {
        final ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPInputStream gis = new GZIPInputStream(bis)) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        }
        return bos.toByteArray();
    }
}
