package util;

/**
 *
 * @author zotta
 */
public class GroupConverter {

    private final String regularCustomer = "0afa8de1-147c-11e8-edec-2b197906d816";
    private final String vipCustomer = "0afa8de1-147c-11e8-edec-201e0f00872c";

    public String convert(String group) {
        if (group.equals("Regular Customers")){
            return regularCustomer;
        }else if(group.equals("VIP Customers")){
            return vipCustomer;
        }
        else return "666";   
            
        }
    }


