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
package ch.quantasy.gateway.service.device.distanceUS;

import ch.quantasy.gateway.service.device.AbstractDeviceService;
import ch.quantasy.tinkerforge.device.distanceUS.DeviceDistanceCallbackThreshold;
import ch.quantasy.tinkerforge.device.distanceUS.DistanceUSDevice;
import ch.quantasy.tinkerforge.device.distanceUS.DistanceUSDeviceCallback;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author reto
 */
public class DistanceUSService extends AbstractDeviceService<DistanceUSDevice, DistanceUSServiceContract> implements DistanceUSDeviceCallback {

    public DistanceUSService(DistanceUSDevice device,URI mqttURI) throws MqttException {

        super(device, new DistanceUSServiceContract(device),mqttURI);
        addDescription(getServiceContract().INTENT_DEBOUNCE_PERIOD, "[0.." + Long.MAX_VALUE + "]");
        addDescription(getServiceContract().INTENT_DISTANCE_CALLBACK_PERIOD, "[0.." + Long.MAX_VALUE + "]");
        addDescription(getServiceContract().INTENT_DISTANCE_THRESHOLD, "option: [x|o|i|<|>]\n min: [0..4095]\n max: [0..4095]");
        addDescription(getServiceContract().INTENT_MOVING_AVERAGE, "[0..100]");
        
        addDescription(getServiceContract().EVENT_DISTANCE, "timestamp: [0.." + Long.MAX_VALUE + "]\n value: [[0..4095]\n");
        addDescription(getServiceContract().EVENT_DISTANCE_REACHED, "timestamp: [0.." + Long.MAX_VALUE + "]\n value: [0..4095]\n");
        addDescription(getServiceContract().STATUS_DISTANCE_CALLBACK_PERIOD, "[0.." + Long.MAX_VALUE + "]");
        addDescription(getServiceContract().STATUS_DISTANCE_THRESHOLD, "option: [x|o|i|<|>]\n min: [0..4095]\n max: [0..4095]");
        addDescription(getServiceContract().STATUS_DEBOUNCE_PERIOD, "[0.." + Long.MAX_VALUE + "]");
        addDescription(getServiceContract().STATUS_MOVING_AVERAGE, "[0..100]");
        
   
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        byte[] payload = mm.getPayload();
        if (payload == null) {
            return;

        }
        try {
            if (string.startsWith(getServiceContract().INTENT_DEBOUNCE_PERIOD)) {

                Long period = getMapper().readValue(payload, Long.class);
                getDevice().setDebouncePeriod(period);
            }
            if (string.startsWith(getServiceContract().INTENT_DISTANCE_CALLBACK_PERIOD)) {

                Long period = getMapper().readValue(payload, Long.class);
                getDevice().setDistanceCallbackPeriod(period);
            }

            if (string.startsWith(getServiceContract().INTENT_DISTANCE_THRESHOLD)) {

                DeviceDistanceCallbackThreshold threshold = getMapper().readValue(payload, DeviceDistanceCallbackThreshold.class);
                getDevice().setDistanceCallbackThreshold(threshold);
            }
            
            if (string.startsWith(getServiceContract().INTENT_MOVING_AVERAGE)) {

                Short movingAverage = getMapper().readValue(payload, Short.class);
                getDevice().setMovingAverage(movingAverage);
            }

        } catch (IOException ex) {
            Logger.getLogger(DistanceUSService.class
                    .getName()).log(Level.SEVERE, null, ex);
            return;
        }

    }


    @Override
    public void debouncePeriodChanged(long period) {
        addStatus(getServiceContract().STATUS_DEBOUNCE_PERIOD, period);
    }

    @Override
    public void distanceCallbackPeriodChanged(long period) {
        addStatus(getServiceContract().STATUS_DISTANCE_CALLBACK_PERIOD, period);
    }

    @Override
    public void distanceCallbackThresholdChanged(DeviceDistanceCallbackThreshold threshold) {
        addStatus(getServiceContract().STATUS_DISTANCE_THRESHOLD, threshold);
    }

    @Override
    public void movingAverageChanged(short movingAverage) {
        addStatus(getServiceContract().STATUS_MOVING_AVERAGE, movingAverage);
    }
    
    @Override
    public void distance(int i) {
        addEvent(getServiceContract().EVENT_DISTANCE, new DisntanceEvent(i));  
    }

    @Override
    public void distanceReached(int i) {
        addEvent(getServiceContract().EVENT_DISTANCE_REACHED, new DisntanceEvent(i));
    }

    class DisntanceEvent {

        protected long timestamp;
        protected int value;

        public DisntanceEvent(int value) {
            this(value, System.currentTimeMillis());
        }

        public DisntanceEvent(int value, long timeStamp) {
            this.value = value;
            this.timestamp = timeStamp;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public int getValue() {
            return value;
        }

    }

}
