package com.example.eu7340.egliseteste.utils;

import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.Perfil;
import com.example.eu7340.egliseteste.Models.Usuario;

public class Sessao {

    public static MyJSONObject ultima_congregacao = null;
    public static MyJSONObject ultima_configuracao = null;
    public static MyJSONArray ultimos_menus = null;
    public static MyJSONObject ultimo_usuario = null;
    public static MyJSONObject ultimo_perfil = null;
    public static MyJSONObject ultimo_membro = null;
    public static MyJSONObject ultima_funcao = null;

    public static boolean existe_login_ativo = false;

}
