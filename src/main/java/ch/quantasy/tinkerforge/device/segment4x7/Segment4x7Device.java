/*
 *   "TiMqWay"
 *
 *    TiMqWay(tm): A gateway to provide an MQTT-View for the Tinkerforge(tm) world (Tinkerforge-MQTT-Gateway).
 *
 *    Copyright (c) 2015 Bern University of Applied Sciences (BFH),
 *    Research Institute for Security in the Information Society (RISIS), Wireless Communications & Secure Internet of Things (WiCom & SIoT),
 *    Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *    Licensed under Dual License consisting of:
 *    1. GNU Affero General Public License (AGPL) v3
 *    and
 *    2. Commercial license
 *
 *
 *    1. This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *    2. Licensees holding valid commercial licenses for TiMqWay may use this file in
 *     accordance with the commercial license agreement provided with the
 *     Software or, alternatively, in accordance with the terms contained in
 *     a written agreement between you and Bern University of Applied Sciences (BFH),
 *     Research Institute for Security in the Information Society (RISIS), Wireless Communications & Secure Internet of Things (WiCom & SIoT),
 *     Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *     For further information contact <e-mail: reto.koenig@bfh.ch>
 *
 *
 */
package ch.quantasy.tinkerforge.device.segment4x7;

import ch.quantasy.tinkerforge.device.generic.GenericDevice;
import ch.quantasy.tinkerforge.device.remoteSwitch.RemoteSwitchDevice;
import ch.quantasy.tinkerforge.stack.TinkerforgeStackAddress;
import com.tinkerforge.BrickletSegmentDisplay4x7;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class Segment4x7Device extends GenericDevice<BrickletSegmentDisplay4x7, Segment4x7DeviceCallback> {

    private DeviceSegments segments;
    private DeviceCounterParameters counterParameter;

    public Segment4x7Device(TinkerforgeStackAddress address, BrickletSegmentDisplay4x7 device) throws NotConnectedException, TimeoutException {
        super(address, device);
    }

    @Override
    protected void addDeviceListeners() {
        getDevice().addCounterFinishedListener(super.getCallback());

    }

    @Override
    protected void removeDeviceListeners() {
        getDevice().removeCounterFinishedListener(super.getCallback());
    }

    public void setSegments(DeviceSegments segments) {
        try {
            getDevice().setSegments(segments.getBits(), segments.getBrightness(), segments.getColon());
            this.segments = new DeviceSegments(getDevice().getSegments());
            getCallback().segmentsChanged(this.segments);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(RemoteSwitchDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public void startCounter(DeviceCounterParameters counter) {
        try {
            getDevice().startCounter(counter.getFrom(),counter.getTo(),counter.getIncrement(),counter.getLength());
            this.counterParameter = counter;
            getCallback().counterStarted(counter);
        } catch (TimeoutException | NotConnectedException ex) {
            Logger.getLogger(RemoteSwitchDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    

}
