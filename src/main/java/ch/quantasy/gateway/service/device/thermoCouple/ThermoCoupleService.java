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
package ch.quantasy.gateway.service.device.thermoCouple;

import ch.quantasy.gateway.service.device.AbstractDeviceService;
import ch.quantasy.tinkerforge.device.thermoCouple.DeviceConfiguration;
import ch.quantasy.tinkerforge.device.thermoCouple.DeviceTemperatureCallbackThreshold;
import ch.quantasy.tinkerforge.device.thermoCouple.ThermoCoupleDevice;
import ch.quantasy.tinkerforge.device.thermoCouple.ThermoCoupleDeviceCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.net.URI;

/**
 *
 * @author reto
 */
public class ThermoCoupleService extends AbstractDeviceService<ThermoCoupleDevice, ThermoCoupleServiceContract> implements ThermoCoupleDeviceCallback {

    public ThermoCoupleService(ThermoCoupleDevice device, URI mqttURI) throws MqttException {

        super(mqttURI, device, new ThermoCoupleServiceContract(device));
    }

    @Override
    public void messageReceived(String string, byte[] payload) throws Exception {

        if (string.startsWith(getContract().INTENT_DEBOUNCE_PERIOD)) {

            Long period = getMapper().readValue(payload, Long.class);
            getDevice().setDebouncePeriod(period);
        }
        if (string.startsWith(getContract().INTENT_TEMPERATURE_CALLBACK_PERIOD)) {

            Long period = getMapper().readValue(payload, Long.class);
            getDevice().setTemperatureCallbackPeriod(period);
        }

        if (string.startsWith(getContract().INTENT_TEMPERATURE_THRESHOLD)) {

            DeviceTemperatureCallbackThreshold threshold = getMapper().readValue(payload, DeviceTemperatureCallbackThreshold.class);
            getDevice().setTemperatureCallbackThreshold(threshold);
        }

        if (string.startsWith(getContract().INTENT_CONFIGURATION)) {

            DeviceConfiguration configuration = getMapper().readValue(payload, DeviceConfiguration.class);
            getDevice().setConfiguration(configuration);
        }

    }

    @Override
    public void debouncePeriodChanged(long period) {
        publishStatus(getContract().STATUS_DEBOUNCE_PERIOD, period);
    }

    @Override
    public void temperatureCallbackPeriodChanged(long period) {
        publishStatus(getContract().STATUS_TEMPERATURE_CALLBACK_PERIOD, period);
    }

    @Override
    public void temperatureCallbackThresholdChanged(DeviceTemperatureCallbackThreshold threshold) {
        publishStatus(getContract().STATUS_TEMPERATURE_THRESHOLD, threshold);
    }

    @Override
    public void configurationChanged(DeviceConfiguration configuration) {
        publishStatus(getContract().STATUS_CONFIGURATION, configuration);
    }

    @Override
    public void temperature(int i) {
        publishEvent(getContract().EVENT_TEMPERATURE, i);
    }

    @Override
    public void temperatureReached(int i) {
        publishEvent(getContract().EVENT_TEMPERATURE_REACHED, i);
    }

    @Override
    public void errorState(boolean bln, boolean bln1) {
        publishEvent(getContract().EVENT_ERROR, new Error(bln, bln1));
    }

    public static class Error {

        protected boolean voltage;
        protected boolean openCircuit;

        public Error(boolean voltage, boolean openCircuit) {
            this.voltage = voltage;
            this.openCircuit = openCircuit;
        }

        public boolean getVoltage() {
            return voltage;
        }

        public boolean getOpenCircuit() {
            return openCircuit;
        }

    }

}
