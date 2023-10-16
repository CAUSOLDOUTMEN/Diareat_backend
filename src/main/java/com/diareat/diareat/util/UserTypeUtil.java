package com.diareat.diareat.util;

import java.util.ArrayList;
import java.util.List;

public class UserTypeUtil {

    public static int decideUserType(int gender, int age){ // 성별과 연령에 따른 유저 타입 분류
        if(gender == 0){ // 남자
            if(age < 11) return 1;
            else if(age < 19) return 2;
            else if(age < 30) return 3;
            else if(age < 50) return 4;
            else if(age < 65) return 5;
            else return 6;
        } else { // 여자
            if(age < 11) return 7;
            else if(age < 19) return 8;
            else if(age < 30) return 9;
            else if(age < 50) return 10;
            else if(age < 65) return 11;
            else return 12;
        }
    }

    public static List<Integer> getStanardByUserType(int type){
        ArrayList<Integer> standard = new ArrayList<>();
        // 칼, 지, 탄의 순서로 3개 저장

        /** 기준섭취량 산정을 위한 type
         * 1. 11세 미만 남성
         * 2. 12~18세 남성
         * 3. 19~29세 남성
         * 4. 30~49세 남성
         * 5. 50~64세 남성
         * 6. 65세 이상 남성
         * 7. 11세 미만 여성
         * 8. 12~18세 여성
         * 9. 19~29세 여성
         * 10. 30~49세 여성
         * 11. 50~64세 여성
         * 12. 65세 이상 여성
         */

        switch(type){
            case 1: // 남자
                standard.add(1630); // 칼로리
                standard.add(45); // 지방
                standard.add(240); // 탄수화물
                break;
            case 2:
                standard.add(1950);
                standard.add(60);
                standard.add(280);
                break;
            case 3:
                standard.add(2070);
                standard.add(60);
                standard.add(270);
                break;
            case 4:
                standard.add(2180);
                standard.add(60);
                standard.add(280);
                break;
            case 5:
                standard.add(1980);
                standard.add(50);
                standard.add(280);
                break;
            case 6:
                standard.add(1610);
                standard.add(30);
                standard.add(260);
                break;
            case 7: // 여자
                standard.add(1430);
                standard.add(40);
                standard.add(210);
                break;
            case 8:
                standard.add(1480);
                standard.add(40);
                standard.add(215);
                break;
            case 9:
                standard.add(1470);
                standard.add(45);
                standard.add(200);
                break;
            case 10:
                standard.add(1500);
                standard.add(40);
                standard.add(210);
                break;
            case 11:
                standard.add(1430);
                standard.add(35);
                standard.add(225);
                break;
            default: // 12
                standard.add(1230);
                standard.add(20);
                standard.add(210);
                break;
        }
        return standard;
    }
}
