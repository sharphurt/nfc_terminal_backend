package ru.catstack.nfc_terminal.model;

import ru.catstack.nfc_terminal.model.audit.DateAudit;
import ru.catstack.nfc_terminal.model.enums.DeviceType;

import javax.persistence.*;

@Entity(name = "session")
@Table(name = "session")
public class Session extends DateAudit {
    @Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "unique_key")
    private long uniqueKey;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "device_type")
    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    public Session() {
    }

    public Session(Long userId, DeviceInfo deviceInfo, long uniqueKey) {
        this.userId = userId;
        this.deviceType = deviceInfo.getDeviceType();
        this.deviceId = deviceInfo.getDeviceId();
        this.uniqueKey = uniqueKey;
    }

    public long getId() {
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

    public long getUniqueKey() {
        return uniqueKey;
    }
}
