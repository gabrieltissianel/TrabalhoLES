
class Credentials {
  String login;
  String senha;

  Credentials(this.login, this.senha);

  Map<String, dynamic> toJson(){
    return {
      'login' : login,
      'senha' : senha
    };
  }

}