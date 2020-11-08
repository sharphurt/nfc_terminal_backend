package ru.catstack.nfc_terminal.model;

import ru.catstack.nfc_terminal.model.audit.DateAudit;

import javax.persistence.*;

@Entity(name = "session")
@Table(name = "session")
public class Session extends DateAudit {
    @Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "device_type")
    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    public Session() {
    }

    public Session(Long userId, DeviceInfo deviceInfo) {
        this.userId = userId;
        this.deviceType = deviceInfo.getDeviceType();
        this.deviceId = deviceInfo.getDeviceId();
    }

    public Long getId() {
        return id;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DeviceInfo toDeviceInfo() {
        return new DeviceInfo(this.deviceId, this.deviceType);
    }

    public Long getUserId() {
        return userId;
    }
}