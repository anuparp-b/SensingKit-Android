/*
 * Copyright (c) 2014. Queen Mary University of London
 * Kleomenis Katevas, k.katevas@qmul.ac.uk
 *
 * This file is part of SensingKit-Android library.
 * For more information, please visit http://www.sensingkit.org
 *
 * SensingKit-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SensingKit-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SensingKit-Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.sensingkit.sensingkitlib.modules;

import android.content.Context;
import android.hardware.SensorEvent;

import org.sensingkit.sensingkitlib.SKException;
import org.sensingkit.sensingkitlib.model.data.AbstractData;
import org.sensingkit.sensingkitlib.model.data.RotationData;

public class Rotation extends AbstractNativeSensorModule {

    private static final String TAG = "Rotation";

    public Rotation(final Context context) throws SKException {
        super(context, SensorModuleType.ROTATION);
    }

    protected AbstractData buildData(SensorEvent event)
    {
        return new RotationData(event.timestamp, event.values[0], event.values[1], event.values[2], event.values[3], event.values[4]);
    }
}
