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

import java.io.IOException;

/**
 * Raised when a character payload still exceeds the maximum QR capacity even
 * after dropping all QR-optional {@code CharacterInfo} fields.
 */
public class CharacterQrPayloadTooLargeException extends IOException {
    private static final long serialVersionUID = 3662309157597657387L;

    private final int actualBytes;
    private final int maxBytes;

    public CharacterQrPayloadTooLargeException(int actualBytes, int maxBytes) {
        super("Character QR payload size " + actualBytes + " bytes exceeds maximum "
                + maxBytes + " bytes.");
        this.actualBytes = actualBytes;
        this.maxBytes = maxBytes;
    }

    public int getActualBytes() {
        return actualBytes;
    }

    public int getMaxBytes() {
        return maxBytes;
    }
}

