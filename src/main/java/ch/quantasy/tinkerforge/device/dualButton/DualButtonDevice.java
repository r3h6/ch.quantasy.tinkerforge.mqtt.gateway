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
package ch.quantasy.tinkerforge.device.dualButton;

import ch.quantasy.tinkerforge.device.generic.GenericDevice;
import ch.quantasy.tinkerforge.stack.TinkerforgeStack;
import com.tinkerforge.BrickletDualButton;

import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class DualButtonDevice extends GenericDevice<BrickletDualButton, DualButtonDeviceCallback> {

    private DeviceLEDState LEDState;

    public DualButtonDevice(TinkerforgeStack stack, BrickletDualButton device) throws NotConnectedException, TimeoutException {
        super(stack, device);
    }

    @Override
    protected void addDeviceListeners(BrickletDualButton device) {
        device.addStateChangedListener(super.getCallback());
        if (LEDState != null) {
            setLEDState(LEDState);
        }

    }

    @Override
    protected void removeDeviceListeners(BrickletDualButton device) {
        device.removeStateChangedListener(super.getCallback());
    }

    public void setLEDState(DeviceLEDState ledState) {
        try {
            getDevice().setLEDState(ledState.led1.getValue(), ledState.led2.getValue());
            this.LEDState = new DeviceLEDState(getDevice().getLEDState());
            super.getCallback().ledStateChanged(this.LEDState);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(DualButtonDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSelectedLEDState(DeviceSelectedLEDStateParameters parameters) {
        try {
            getDevice().setSelectedLEDState(parameters.getLed(), parameters.getState().getValue());
            this.LEDState = new DeviceLEDState(getDevice().getLEDState());
            super.getCallback().ledStateChanged(this.LEDState);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(DualButtonDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
