import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * バッキングビーン
 * このクラスの処理でログインがされているかのチェックを行っている。
 */
@LoginCheck
@Named
@RequestScoped
public class UserBackingBean
{
	@NotNull
	@Min(1)
	@Max(99999)
	private Integer number;
	@NotBlank
	private String name;
	@NotBlank
	@Email
	private String mail;
	
	@Inject
	Inserter inserter;
	
	public String transition()
	{
		// 何かしらの処理
		//
		return "xxx.xhtml";
	}

	public void clear()
	{
		number = null;
		name = mail = null;
	}
	
	public Integer getNumber()
	{
		return number;
	}
	
	public void setNumber(Integer number)
	{
		this.number = number;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getMail()
	{
		return mail;
	}
	
	public void setMail(String mail)
	{
		this.mail = mail;
	}
}
