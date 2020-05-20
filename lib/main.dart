import 'package:flutter/material.dart';
import 'package:flutter_file_preview/yiyun_file_preview_plugin/yiyun_file_preview.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
        // This makes the visual density adapt to the platform that you run
        // the app on. For desktop platforms, the controls will be smaller and
        // closer together (more dense) than on mobile platforms.
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {


  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(appBar: new AppBar(
      title: new Text('FilePreview'),
    ),
        body: new Center(
          child: new Column(
            children: <Widget>[
              new Container(
                child: new MaterialButton(
                    onPressed: open, child: new Text("路径错误")),
                padding: const EdgeInsets.all(8.0),
              ),
              new Container(
                child: new MaterialButton(
                  onPressed: openFile,
                  child: new Text("选择文件预览"),
                ),
                padding: const EdgeInsets.all(8.0),
              )
            ],
          ),
        )// This trailing comma makes auto-formatting nicer for build methods.
    );
  }

  Future open() async{

    if(await YiyunFilePreview.persiomReadFile()){
      await YiyunFilePreview.GetFilePath().then((path) async {
        path = "";
        if(path!=null){
          if(await YiyunFilePreview.IfFileExists(path)){
            if(await YiyunFilePreview.CanOpenFile(path)){
              YiyunFilePreview.fileRead(path);
            }
          }
        }
      });
    }else{
      print("没有文件读取权限");
      await YiyunFilePreview.persionReadFileRequest();
    }
  }

  Future openFile() async{
    if(await YiyunFilePreview.persiomReadFile()){
      await YiyunFilePreview.GetFilePath().then((path) async {

        if(path!=null){
          if(await YiyunFilePreview.IfFileExists(path)){
            if(await YiyunFilePreview.CanOpenFile(path)){
              YiyunFilePreview.fileRead(path);
            }
          }
        }
      });
    }else{
      print("没有文件读取权限");
      await YiyunFilePreview.persionReadFileRequest();
    }
  }

}
