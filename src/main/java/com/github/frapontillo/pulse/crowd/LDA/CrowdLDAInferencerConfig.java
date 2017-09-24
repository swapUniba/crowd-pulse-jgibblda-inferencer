package com.github.frapontillo.pulse.crowd.LDA;

import com.github.frapontillo.pulse.spi.IPluginConfig;
import com.github.frapontillo.pulse.spi.PluginConfigHelper;
import com.google.gson.JsonElement;

public class CrowdLDAInferencerConfig implements IPluginConfig<CrowdLDAInferencerConfig>{

	
	private String model="model";
	@Override
	public CrowdLDAInferencerConfig buildFromJsonElement(JsonElement json) {
		return PluginConfigHelper.buildFromJson(json, CrowdLDAInferencerConfig.class);
	}
	
	public void setModel(String m){
		this.model=m;
	}
	
	public String getModel(){
		return this.model;
	}

}
