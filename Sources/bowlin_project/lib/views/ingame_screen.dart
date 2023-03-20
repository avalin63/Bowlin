import 'package:bowl_in/main.dart';
import 'package:bowl_in/model/GameDetail.dart';
import 'package:bowl_in/model/GamePlayer.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';

import '../model/Game.dart';
import '../model/Guest.dart';
import '../widgets/button_new_party.dart';
import '../widgets/ingame_widgets.dart';
import '../widgets/scores_list_widget.dart';

class InGameScreen extends StatefulWidget {
  const InGameScreen({Key? key}) : super(key: key);

  @override
  State<InGameScreen> createState() => _InGameScreenState();
}

class _InGameScreenState extends State<InGameScreen> {
  late Widget widgetHolder;

  void initState() {
    widgetHolder = InGameCardConfig();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Container(
          width: MediaQuery.of(context).size.width,
          height: MediaQuery.of(context).size.height,
          decoration: const BoxDecoration(
              gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [
              Color(0xff19BDE0),
              Color(0xff4A17DC),
            ],
          )),
        ),
        Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(
              height: 120,
            ),
            widgetHolder,
            Spacer(),
            ElevatedButton(
              onPressed: () {
                GameDetail gd = GameDetail(1, DateTime.now(), null, 123, false,
                    MyApp.controller.userCurrent.id, [MyApp.controller.userCurrent, new Guest("Louison")]);

                MyApp.controller.gamePlayer.game = gd;
                MyApp.controller.gameCurrent = gd;
                MyApp.controller.gamePlayer.onNext(false, context);
              },
              child: Text(
                "PLAY",
                style: GoogleFonts.roboto(
                    fontWeight: FontWeight.bold,
                    fontSize: 40,
                    color: Color(0xff1ABAE0)),
              ),
              style: ElevatedButton.styleFrom(
                side: BorderSide(
                  width: 7,
                  color: Color(0xff1ABAE0),
                ),
                onPrimary: Colors.transparent,
                primary: Colors.transparent,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(55),
                ),
                minimumSize: Size(200, 80),
              ),
            ),
            Spacer(),
          ],
        )
      ],
    );
  }
}
