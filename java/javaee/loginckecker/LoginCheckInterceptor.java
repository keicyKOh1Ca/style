import java.io.Serializable;
import javax.annotation.Priority;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import com.nis_nim.common.exception.NoLoginInfoException;
import com.nis_nim.common.info.UserInfoSession;

/**
 * 未ログインチェックインターセプタ<br>
 */
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@LoginInterceptor
public class LoginCheckInterceptor implements Serializable
{
	@Inject
	LoginInfo loginInfo;
	
	@AroundInvoke
	public Object obj(InvocationContext ic) throws Exception
	{
		ProjectStage myStage = FacesContext.getCurrentInstance().getApplication().getProjectStage();
		// web.xmlにて設定したパラメータ
    // <context-param>
		// <param-name>javax.faces.PROJECT_STAGE</param-name>
		// <param-value>Production</param-value>
	  // </context-param>
    // ↑本番モードの場合にチェック↓
		if(myStage.equals(ProjectStage.Production)){
			//ログイン情報がなければエラーページへ
			if (loginInfo.getId() == null)
			{
				throw new UserSpecifyException();
			}
		}
		Object result = ic.proceed();
		return result;
	}
}
