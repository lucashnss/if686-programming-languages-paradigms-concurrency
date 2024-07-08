class EncodeCipherEightCharacter {
    public static void main(String args[]) {
        String msg = "This is a test";
        String encmsg = "";
        String decmsg = "";
        String string_key = "12345678";
        System.out.print("Original message: ");
        System.out.println(msg);

        for(int i=0; i < msg.length(); i++)
            encmsg = encmsg + (char) (msg.charAt(i) ^ string_key.charAt(i % string_key.length()));
        System.out.print("Encoded message: ");
        System.out.println(encmsg);

        for(int i=0; i < msg.length(); i++)
            decmsg = decmsg + (char) (encmsg.charAt(i) ^ string_key.charAt(i % string_key.length()));
        System.out.print("Decoded message: ");
        System.out.println(decmsg);
    }
}
