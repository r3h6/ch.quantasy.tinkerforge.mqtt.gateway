/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.gateway.tinkerforge;

import ch.quantasy.gateway.service.device.ambientLight.AmbientLightService;
import ch.quantasy.gateway.service.device.ambientLightV2.AmbientLightV2Service;
import ch.quantasy.gateway.service.device.barometer.BarometerService;
import ch.quantasy.gateway.service.device.co2.CO2Service;
import ch.quantasy.gateway.service.device.dualRelay.DualRelayService;
import ch.quantasy.gateway.service.device.humidity.HumidityService;
import ch.quantasy.gateway.service.device.ledStrip.LEDStripService;
import ch.quantasy.gateway.service.device.moisture.MoistureService;
import ch.quantasy.gateway.service.device.motionDetector.MotionDetectorService;
import ch.quantasy.gateway.service.device.remoteSwitch.RemoteSwitchService;
import ch.quantasy.gateway.service.device.dualButton.DualButtonService;
import ch.quantasy.tinkerforge.device.humidity.HumidityDevice;
import ch.quantasy.tinkerforge.device.TinkerforgeDevice;
import ch.quantasy.tinkerforge.device.TinkerforgeDeviceListener;
import ch.quantasy.tinkerforge.device.ambientLight.AmbientLightDevice;
import ch.quantasy.tinkerforge.device.ambientLightV2.AmbientLightV2Device;
import ch.quantasy.tinkerforge.device.barometer.BarometerDevice;
import ch.quantasy.tinkerforge.device.co2Device.CO2Device;
import ch.quantasy.tinkerforge.device.dualButton.DualButtonDevice;
import ch.quantasy.tinkerforge.device.dualRelay.DualRelayDevice;
import ch.quantasy.tinkerforge.device.led.LEDStripDevice;
import ch.quantasy.tinkerforge.device.moisture.MoistureDevice;
import ch.quantasy.tinkerforge.device.motionDetector.MotionDetectorDevice;
import ch.quantasy.tinkerforge.device.remoteSwitch.RemoteSwitchDevice;
import ch.quantasy.tinkerforge.factory.TinkerforgeStackFactory;
import ch.quantasy.tinkerforge.stack.TinkerforgeStack;
import ch.quantasy.tinkerforge.stack.TinkerforgeStackAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 */
public class TinkerForgeManager implements TinkerforgeDeviceListener {

    private final Set<TinkerforgeFactoryListener> listeners;

    private final TinkerforgeStackFactory stackFactory;
    private static TinkerForgeManager instance = new TinkerForgeManager();

    static {
        instance = new TinkerForgeManager();
    }

    private TinkerForgeManager() {
        stackFactory = TinkerforgeStackFactory.getInstance();
        this.listeners = new HashSet<>();
    }

    public static TinkerForgeManager getInstance() {
        return instance;
    }

    public void addListener(TinkerforgeFactoryListener listener) {
        this.listeners.add(listener);

    }

    public void removeListener(TinkerforgeFactoryListener listener) {
        this.listeners.remove(listener);
    }

    public TinkerforgeStack addStack(TinkerforgeStackAddress address) {
        TinkerforgeStack stack = null;
        if (stackFactory.addStack(address)) {
            stack = stackFactory.getStack(address);
            for (TinkerforgeFactoryListener listener : listeners) {
                listener.stackAdded(stack);
            }
            stack.addListener(this);
            stack.connect();
        }
        return stackFactory.getStack(address);
    }

    public TinkerforgeStack removeStack(TinkerforgeStackAddress address) {
        TinkerforgeStack stack = stackFactory.removeStack(address);
        if (stack != null) {
            stack.disconnect();
            for (TinkerforgeFactoryListener listener : listeners) {
                listener.stackRemoved(stack);
            }
            stack.removeListener(this);
        }
        return stack;
    }

    public boolean containsStack(TinkerforgeStackAddress address) {
        return stackFactory.containsStack(address);
    }

    public Set<TinkerforgeStackAddress> getStackAddresses() {
        Set<TinkerforgeStackAddress> stackAddresses = new HashSet<>();
        for (TinkerforgeStack stack : stackFactory.getTinkerforgeStacks()) {
            stackAddresses.add(stack.getStackAddress());
        }
        return stackAddresses;
    }

    public TinkerforgeStackFactory getStackFactory() {
        return stackFactory;
    }

    private Set<TinkerforgeDevice> managedDevices = new HashSet<>();

    @Override
    public void connected(TinkerforgeDevice tinkerforgeDevice) {
        if (managedDevices.contains(tinkerforgeDevice)) {
            return;
        }
        managedDevices.add(tinkerforgeDevice);
        try {
            
            if (tinkerforgeDevice instanceof HumidityDevice) {
                HumidityService service=new HumidityService((HumidityDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof LEDStripDevice) {
                LEDStripService service=new LEDStripService((LEDStripDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof MotionDetectorDevice) {
                MotionDetectorService service=new MotionDetectorService((MotionDetectorDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof MoistureDevice) {
                MoistureService service=new MoistureService((MoistureDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof AmbientLightDevice) {
                AmbientLightService service=new AmbientLightService((AmbientLightDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof AmbientLightV2Device) {
                AmbientLightV2Service service=new AmbientLightV2Service((AmbientLightV2Device) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof RemoteSwitchDevice) {
                RemoteSwitchService service=new RemoteSwitchService((RemoteSwitchDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof DualRelayDevice) {
                DualRelayService service=new DualRelayService((DualRelayDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof BarometerDevice) {
                BarometerService service=new BarometerService((BarometerDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof DualButtonDevice) {
                DualButtonService service=new DualButtonService((DualButtonDevice) tinkerforgeDevice);
                System.out.println(service);
            }
            if (tinkerforgeDevice instanceof CO2Device) {
                CO2Service service=new CO2Service((CO2Device) tinkerforgeDevice);
                System.out.println(service);
            }
        } catch (MqttException ex) {
            Logger.getLogger(TinkerForgeManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void reConnected(TinkerforgeDevice tinkerforgeDevice) {
        //Nothing to do
    }

    @Override
    public void disconnected(TinkerforgeDevice tinkerforgeDevice) {
//Nothing to do 
    }
}
