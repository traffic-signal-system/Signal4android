package com.aiton.administrator.shane_library.shane.upgrade;

public class Upgrade {
	int version;
	String feature;
	String apkurl;
	int filelen;

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public String getFeature()
	{
		return feature;
	}

	public void setFeature(String feature)
	{
		this.feature = feature;
	}

	public String getApkurl()
	{
		return apkurl;
	}

	public void setApkurl(String apkurl)
	{
		this.apkurl = apkurl;
	}

	public int getFilelen()
	{
		return filelen;
	}

	public void setFilelen(int filelen)
	{
		this.filelen = filelen;
	}

}
