package com.example.eu7340.egliseteste.utils;

import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.Perfil;
import com.example.eu7340.egliseteste.Models.Usuario;

public class Sessao {

    public static Congregacao ultima_congregacao = null;
    public static Configuracao ultima_configuracao = null;
    public static Usuario ultimo_usuario = null;
    public static Perfil ultimo_perfil = null;
    public static Membro ultimo_membro = null;
    public static Funcao ultima_funcao = null;

    public static boolean existe_login_ativo = false;

}
