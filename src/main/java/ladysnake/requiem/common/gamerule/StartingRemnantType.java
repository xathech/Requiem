/*
 * Requiem
 * Copyright (C) 2017-2020 Ladysnake
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses>.
 */
package ladysnake.requiem.common.gamerule;

import ladysnake.requiem.api.v1.remnant.RemnantType;
import ladysnake.requiem.common.remnant.RemnantTypes;

import javax.annotation.Nullable;

public enum StartingRemnantType {
    CHOOSE(null), FORCE_REMNANT(RemnantTypes.REMNANT), FORCE_VANILLA(RemnantTypes.MORTAL);

    private final RemnantType remnantType;

    StartingRemnantType(@Nullable RemnantType remnantType) {
        this.remnantType = remnantType;
    }

    @Nullable
    public RemnantType getRemnantType() {
        return remnantType;
    }
}
