#!/vendor/bin/sh
# Generate a unique locally-administered WLAN MAC address if one
# does not already exist in the persist partition.
#
# The cnss_utils driver warns "WLAN MAC address is not set" when
# /mnt/vendor/persist/wlan_mac.bin is missing, causing all devices
# to fall back to the same hardcoded MAC from WCNSS_qcom_cfg.ini.

MAC_FILE="/mnt/vendor/persist/wlan_mac.bin"

if [ -s "$MAC_FILE" ]; then
    exit 0
fi

# Generate a random locally-administered unicast MAC
# Byte 0: set bit 1 (locally administered), clear bit 0 (unicast)
RANDOM_HEX=$(cat /proc/sys/kernel/random/uuid | tr -d '-' | head -c 12)
BYTE0=$(printf '%02x' $(( (0x${RANDOM_HEX:0:2} | 0x02) & 0xfe )))
MAC="${BYTE0}:${RANDOM_HEX:2:2}:${RANDOM_HEX:4:2}:${RANDOM_HEX:6:2}:${RANDOM_HEX:8:2}:${RANDOM_HEX:10:2}"

# Write in the format expected by cnss_utils
echo "Intf0MacAddress=${MAC}" > "$MAC_FILE"
echo "END" >> "$MAC_FILE"

chmod 0644 "$MAC_FILE"
chown system:wifi "$MAC_FILE"
