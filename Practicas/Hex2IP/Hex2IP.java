package Hex2IP;

public class Hex2IP {
    public static void main(String[] args) {

        Hex2IP hex2IP = new Hex2IP();
        String op = args[0];
        String cadena = args[1];

        if(op.equals("-hex")){
            hex2IP.hexToIP(cadena);
        }
        else if(op.equals("-ip")){
            hex2IP.ipToHex(cadena);
        }
        else {
            System.out.println("Error: Invalid option. Use -hex or -ip.");
        }
    }

    public void hexToIP(String hex){
        StringBuilder ip = new StringBuilder();
        if(hex.length() != 8){
            System.out.println("Error: Hex string must be 8 characters long.");
            return;
        }
        for(int i = 0; i < hex.length(); i+=2){
            String sub = hex.substring(i, i+2);
            try {
                int decimal = Integer.parseInt(sub, 16);
                if(decimal <= 255){
                    ip.append(decimal);

                    if(i < hex.length() - 2){
                        ip.append(".");
                    }
                }
                else {
                    System.out.println("Error: Invalid hex value for IP address.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid hex string.");
                return;
            }
        }
        System.out.println(ip.toString());
    }
    
    public void ipToHex(String ip){
        if(ip.split("\\.").length != 4){
            System.out.println("Error: Invalid IP address format.");
            return;
        }
        StringBuilder hex = new StringBuilder();
        String[] octets = ip.split("\\.");
        for(String octet : octets){
            int decimal = Integer.parseInt(octet);
            if(decimal >= 0 && decimal <= 255){
                String hexValue = Integer.toHexString(decimal);
                if(hexValue.length() == 1){
                    hex.append("0");
                }
                hex.append(hexValue);
            }
            else {
                System.out.println("Error: Invalid IP address.");
                return;
            }
        }
        String hexString = hex.toString();
        System.out.println(hexString.toUpperCase());
    }
}
