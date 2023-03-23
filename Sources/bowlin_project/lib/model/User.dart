import 'package:bowl_in/database/fields/UserFields.dart';

import 'Achievement.dart';
import 'Game.dart';
import 'Player.dart';
import 'Stat.dart';

class User extends Player {
  final int _id;
  String _mail;
  List<Achievement> _achievements = <Achievement>[];
  List<User> _friends = <User>[];
  final Stat _stat = Stat.empty();
  List<Game> games = [];

  // Constructor
  User(this._id, String name, String image, this._mail, this._achievements,
      this._friends)
      : super(name, image);

  int get id => _id;

  // Getters and setters
  String get mail => _mail;

  set mail(String value) {
    _mail = value;
  }

  List<Achievement> get achievements => _achievements;

  set achievements(List<Achievement> value) {
    _achievements = value;
  }

  List<User> get friends => _friends;

  set friends(List<User> value) {
    _friends = value;
  }

  Stat get stat => _stat;

}
