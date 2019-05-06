TB_Log_Class = {
	diag_log ["TB_ACV_Class", _this];
};
TB_Log_Property = {
	diag_log ["TB_ACV_Property_Non_Inhereted", _this];
};

TB_process_Class = {
	([_this, inheritsFrom _this]) call TB_Log_Class;
	
	{
		if(isNumber _x) then {([_x, (getNumber _x)]) call TB_Log_Property};
		if(isText _x) then {([_x, (getText _x)]) call TB_Log_Property};
		if(isArray _x) then {([_x, (getArray _x)]) call TB_Log_Property};
		if(isClass _x) then {_x call TB_process_Class};
	}forEach (configProperties [_this, "true", false]);
};

(configFile >> "CfgVehicles") call TB_process_Class;
(configFile >> "CfgWeapons") call TB_process_Class;
(configFile >> "CfgMagazines") call TB_process_Class;
(configFile >> "CfgAmmo") call TB_process_Class;
