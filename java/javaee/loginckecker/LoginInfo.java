import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

/**
 * ログイン後セッション情報
 */
@SessionScoped
public class LoginInfo implements Serializable
{
	private String id;

	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
}
