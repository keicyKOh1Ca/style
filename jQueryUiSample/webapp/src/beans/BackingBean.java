package beans;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class BackingBean
{
	private boolean search_flg = false;
	private Integer number;
	private String name;
	private Integer tel;
	private String addr;
	
	private List<Info> info;
	
	{
		// 初期化リスト
	}
	
	public String next()
	{
		// バックエンド処理
^		search_flg = true;
		info = new ArrayList<>();
		info.add(new Info("info_1", "社員番号"));
		info.add(new Info("info_2", "氏名"));
		info.add(new Info("info_3", "住所"));
		info.add(new Info("info_4", "電話番号"));
		return null;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Integer getNumber()
	{
		return number;
	}
	
	public void setNumber(Integer number)
	{
		this.number = number;
	}
	
	public Integer getTel()
	{
		return tel;
	}
	
	public void setTel(Integer tel)
	{
		this.tel = tel;
	}
	
	public String getAddr()
	{
		return addr;
	}
	
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
	
	public List<Info> getInfo()
	{
		return info;
	}
	
	public void setInfo(List<Info> info)
	{
		this.info = info;
	}
	
	public boolean isSearch_flg()
	{
		return search_flg;
	}
	
	public void setSearch_flg(boolean search_flg)
	{
		this.search_flg = search_flg;
	}
}
