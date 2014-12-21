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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import org.sensingkit.sensingkitlib.SKException;
import org.sensingkit.sensingkitlib.SKExceptionErrorCode;
import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.model.data.AbstractData;
import org.sensingkit.sensingkitlib.model.data.DataInterface;

public abstract class AbstractNativeSensorModule extends AbstractSensorModule {

    @SuppressWarnings("unused")
    private static final String TAG = "AbstractNativeSensorModule";

    private final SensorManager mSensorManager;
    private final Sensor mSensor;
    private final SensorEventListener mSensorEventListener;

    protected AbstractNativeSensorModule(final Context context, final SensorModuleType sensorType) throws SKException {
        super(context, sensorType);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(getSensorType(moduleType));

        mSensorEventListener = new SensorEventListener() {

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Ignore
            }

            @Override
            public void onSensorChanged(SensorEvent event) {

                // Build the data object
                DataInterface data = buildData(event);

                // CallBack with data as parameter
                for (SKSensorDataListener callback : callbackList) {
                    callback.onDataReceived(sensorType, data);
                }
            }
        };
    }

    public boolean startSensing() {

        this.isSensing = true;

        return mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSensing() {

        mSensorManager.unregisterListener(mSensorEventListener);

        this.isSensing = false;
    }

    protected abstract AbstractData buildData(SensorEvent event);

    private static int getSensorType(SensorModuleType sensorType) throws SKException{

        switch (sensorType) {

            case ACCELEROMETER:
                return Sensor.TYPE_ACCELEROMETER;

            case GRAVITY:
                return Sensor.TYPE_GRAVITY;

            case LINEAR_ACCELERATION:
                return Sensor.TYPE_LINEAR_ACCELERATION;

            case GYROSCOPE:
                return Sensor.TYPE_GYROSCOPE;

            case ROTATION:
                return Sensor.TYPE_ROTATION_VECTOR;

            case MAGNETOMETER:
                return Sensor.TYPE_MAGNETIC_FIELD;

            case AMBIENT_TEMPERATURE:
                return Sensor.TYPE_AMBIENT_TEMPERATURE;

            case STEP_DETECTOR:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return Sensor.TYPE_STEP_DETECTOR;
                }
                else
                {
                    throw new SKException(TAG, "STEP_DETECTOR requires Android KitKat or greater.", SKExceptionErrorCode.UNKNOWN_ERROR);
                }

            case STEP_COUNTER:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return Sensor.TYPE_STEP_COUNTER;
                }
                else
                {
                    throw new SKException(TAG, "STEP_COUNTER requires Android KitKat or greater.", SKExceptionErrorCode.UNKNOWN_ERROR);
                }

            case LIGHT:
                return Sensor.TYPE_LIGHT;

            case LOCATION:
            case ACTIVITY:
            case BATTERY:
                throw new SKException(TAG, "Not a native SensorModule.", SKExceptionErrorCode.UNKNOWN_ERROR);

            default:
                throw new SKException(TAG, "Unknown SensorModule", SKExceptionErrorCode.UNKNOWN_ERROR);

        }
    }

}