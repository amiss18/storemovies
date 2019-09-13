package com.subtitlor.utilities;

public enum Language { 
	DE("Allemand"), EN("Anglais"), ES("Espagnol"), FR("Français");
	
	
    public String name = null; //description
	
	private Language( String name ) {
		this.name = name;
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
