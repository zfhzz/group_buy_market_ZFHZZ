package cn.bugstack.infrastructure.dcc;

import cn.bugstack.types.annotations.DCCValue;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.stereotype.Service;

@Service
public class DCCService {
    @DCCValue("downgradeSwitch:0")
    private  String downgradeSwitch;

    @DCCValue("cutRange:100")
    private String cutRange;

    public boolean isDowngradeSwitch(){
        return "1".equals(downgradeSwitch);
    }

    public boolean isCutRange(String userId){
        int hashCode = Math.abs(userId.hashCode());
        int lastTwoDigitals = hashCode%100;
        if(lastTwoDigitals <= Integer.parseInt(cutRange)){
            return true;
        }
        return false;
    }
}

