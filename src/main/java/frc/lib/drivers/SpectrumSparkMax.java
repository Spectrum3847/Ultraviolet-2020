package frc.lib.drivers;

import com.revrobotics.CANSparkMax;

public class SpectrumSparkMax extends CANSparkMax{

    public SpectrumSparkMax(int deviceNumber, MotorType m){
        super(deviceNumber, m);
        //this.setParameter(ConfigParameter.kCompensatedNominalVoltage, 12.0);
        this.enableVoltageCompensation(12.0);
        this.restoreFactoryDefaults();
    }
    public SpectrumSparkMax(int deviceNumber){
        this(deviceNumber, MotorType.kBrushless);
    }

} 