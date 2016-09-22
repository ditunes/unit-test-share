package person.ditunes.example.badDesign;

/**
 * Created by linhan on 16/9/20.
 */
public class ResetUserPasswordService {


    public void reset(String userId){
        if(notExistUser(userId)){
            throw new RuntimeException("user not exist");
        }else{
            String getUserPhone = getUserPhone(userId);
            try  {
                RoomySmsClientUtil.sendSms(getUserPhone,"reset password as 1111",null, null,"2014-09-02","qa",null);
            }catch (Exception e){
                throw resolveExpcetion(e);
            }
            sendSuccessResetMsg();
        }
    }

    private void sendSuccessResetMsg() {

    }

    private RuntimeException resolveExpcetion(Exception e) {
        return new RuntimeException(e);
    }


    private String getUserPhone(String userId) {
        return  "18106981399";
    }

    private boolean notExistUser(String userId) {
        return false;
    }

}
