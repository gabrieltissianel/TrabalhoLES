
class Entity{
  int? id;

  Entity({this.id});

  factory Entity.fromJson(Map<dynamic, dynamic> json){
    return Entity(id: json['id']);
  }

  Map<String, dynamic> toJson(){
    return {
      'id' : id
    };
  }
}