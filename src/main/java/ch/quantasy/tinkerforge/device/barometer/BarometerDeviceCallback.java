/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.tinkerforge.device.barometer;

import ch.quantasy.tinkerforge.device.generic.DeviceCallback;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletBarometer.AirPressureCallbackThreshold;
import com.tinkerforge.BrickletBarometer.AltitudeCallbackThreshold;
import com.tinkerforge.BrickletBarometer.Averaging;

/**
 *
 * @author reto
 */
public interface BarometerDeviceCallback extends DeviceCallback, BrickletBarometer.AirPressureListener, BrickletBarometer.AirPressureReachedListener, BrickletBarometer.AltitudeListener, BrickletBarometer.AltitudeReachedListener {

    public void airPressureCallbackPeriodChanged(long period);

    public void altitudeCallbackPeriodChanged(long period);

    public void debouncePeriodChanged(long period);

    public void airPressureCallbackThresholdChanged(AirPressureCallbackThreshold threshold);

    public void altitudeCallbackThresholdChanged(AltitudeCallbackThreshold threshold);

    public void averagingChanged(Averaging averaging);

}
