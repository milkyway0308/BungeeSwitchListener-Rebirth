package skywolf46.bsl.global;

public class BungeeVariables {
    // Identifier
    public static final int BUNGEE_IDENTIFIER_01 = 1194722;
    public static final int BUNGEE_IDENTIFIER_02 = 9987122;
    public static final int BUNGEE_IDENTIFIER_03 = 7236171;
    public static final int BUNGEE_IDENTIFIER_04 = 3419781;

    // Packet ID Parameters

    // PACKET_VALIDATION
    // Client side packet
    // PacketValidation is validation request to server.
    // When server receive, server will return result with PacketValidationResult.
    // Packet Structure {
    //   String(ID), Integer (Port)
    // }
    public static final int PACKET_VALIDATION = 0x0000;

    // PACKET_VALIDATION_RESULT
    // Client side packet
    // PacketValidationResult is validation result packet.
    // If false, server not allows more packet.
    // If true, validation succeed.
    // Packet Structure {
    //   Boolean(isValidated),
    // }
    public static final int PACKET_VALIDATION_RESULT = 0x0001;
    public static final int PACKET_GLOBAL_PAYLOAD = 0x0002;
    public static final int PACKET_TRANSFER_PAYLOAD = 0x0003;
    public static final int PACKET_RECEIVE_PAYLOAD = 0x0004;
    public static final int PACKET_BROADCAST_PACKET = 0x0005;
    public static final int PACKET_REPLY_WRAPPER = 0x0006;
    public static final int PACKET_RELAY = 0x0007;
    public static final int PACKET_RECONSTRUCT = 0x0008;
}
