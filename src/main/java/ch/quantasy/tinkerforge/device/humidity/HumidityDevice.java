/*
 * /*
 *  *   "TiMqWay"
 *  *
 *  *    TiMqWay(tm): A gateway to provide an MQTT-View for the Tinkerforge(tm) world (Tinkerforge-MQTT-Gateway).
 *  *
 *  *    Copyright (c) 2016 Bern University of Applied Sciences (BFH),
 *  *    Research Institute for Security in the Information Society (RISIS), Wireless Communications & Secure Internet of Things (WiCom & SIoT),
 *  *    Quellgasse 21, CH-2501 Biel, Switzerland
 *  *
 *  *    Licensed under Dual License consisting of:
 *  *    1. GNU Affero General Public License (AGPL) v3
 *  *    and
 *  *    2. Commercial license
 *  *
 *  *
 *  *    1. This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU Affero General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU Affero General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU Affero General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *  *    2. Licensees holding valid commercial licenses for TiMqWay may use this file in
 *  *     accordance with the commercial license agreement provided with the
 *  *     Software or, alternatively, in accordance with the terms contained in
 *  *     a written agreement between you and Bern University of Applied Sciences (BFH),
 *  *     Research Institute for Security in the Information Society (RISIS), Wireless Communications & Secure Internet of Things (WiCom & SIoT),
 *  *     Quellgasse 21, CH-2501 Biel, Switzerland.
 *  *
 *  *
 *  *     For further information contact <e-mail: reto.koenig@bfh.ch>
 *  *
 *  *
 */
package ch.quantasy.tinkerforge.device.humidity;

import ch.quantasy.tinkerforge.device.generic.GenericDevice;
import ch.quantasy.tinkerforge.stack.TinkerforgeStack;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class HumidityDevice extends GenericDevice<BrickletHumidity, HumidityDeviceCallback> {

    private Long analogCallbackPeriod;
    private Long humidityCallbackPeriod;
    private Long debouncePeriod;
    private DeviceAnalogValueCallbackThreshold analogValueThreshold;
    private DeviceHumidityCallbackThreshold humidityThreshold;

    public HumidityDevice(TinkerforgeStack stack, BrickletHumidity device) throws NotConnectedException, TimeoutException {
        super(stack, device);
    }

    @Override
    protected void addDeviceListeners(BrickletHumidity device) {
        device.addAnalogValueListener(super.getCallback());
        device.addAnalogValueReachedListener(super.getCallback());
        device.addHumidityListener(super.getCallback());
        device.addHumidityReachedListener(super.getCallback());
        if (analogCallbackPeriod != null) {
            setAnalogValueCallbackPeriod(analogCallbackPeriod);
        }
        if (humidityCallbackPeriod != null) {
            setHumidityCallbackPeriod(this.humidityCallbackPeriod);
        }
        if (debouncePeriod != null) {
            setDebouncePeriod(debouncePeriod);
        }
        if (analogValueThreshold != null) {
            setAnalogValueThreshold(analogValueThreshold);
        }
        if (humidityThreshold != null) {
            setHumidityCallbackThreshold(humidityThreshold);
        }
    }

    @Override
    protected void removeDeviceListeners(BrickletHumidity device) {
        device.removeAnalogValueListener(super.getCallback());
        device.removeAnalogValueReachedListener(super.getCallback());
        device.removeHumidityListener(super.getCallback());
        device.removeHumidityReachedListener(super.getCallback());
    }

    public void setDebouncePeriod(Long period) {
        try {
            getDevice().setDebouncePeriod(period);
            this.debouncePeriod = getDevice().getDebouncePeriod();
            super.getCallback().debouncePeriodChanged(this.debouncePeriod);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(HumidityDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAnalogValueCallbackPeriod(Long period) {
        try {
            getDevice().setAnalogValueCallbackPeriod(period);
            this.analogCallbackPeriod = getDevice().getAnalogValueCallbackPeriod();
            super.getCallback().analogValueCallbackPeriodChanged(this.analogCallbackPeriod);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(HumidityDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setHumidityCallbackPeriod(Long period) {
        try {
            getDevice().setHumidityCallbackPeriod(period);
            this.humidityCallbackPeriod = getDevice().getHumidityCallbackPeriod();
            super.getCallback().humidityCallbackPeriodChanged(this.humidityCallbackPeriod);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(HumidityDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAnalogValueThreshold(DeviceAnalogValueCallbackThreshold threshold) {
        try {
            getDevice().setAnalogValueCallbackThreshold(threshold.getOption(), threshold.getMin(), threshold.getMax());
            this.analogValueThreshold = new DeviceAnalogValueCallbackThreshold(getDevice().getAnalogValueCallbackThreshold());
            super.getCallback().analogValueCallbackThresholdChanged(this.analogValueThreshold);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(HumidityDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setHumidityCallbackThreshold(DeviceHumidityCallbackThreshold threshold) {
        try {
            getDevice().setHumidityCallbackThreshold(threshold.getOption(), threshold.getMin(), threshold.getMax());
            this.humidityThreshold = new DeviceHumidityCallbackThreshold(getDevice().getHumidityCallbackThreshold());
            super.getCallback().humidityCallbackThresholdChanged(this.humidityThreshold);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(HumidityDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
