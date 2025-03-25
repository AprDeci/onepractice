package top.aprdec.onepractice.commmon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class AResult<T> {
    private  Integer code;
    private  String msg;
    private  T data;

    public static <T> AResult<T> success(T data){
        return new AResult<>(200,"success",data);
    }
    public static <T> AResult<T> success(){
        return new AResult<>(200,"success" ,null);
    }


}