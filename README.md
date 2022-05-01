# OOS-src.SnakeGame
과제방입니다!


Guidelines to install and run our game

1. external Libraries
- LWJGL : 우리 SnakeGame의 메인 GUI를 담당하는 library
  https://www.lwjgl.org/download
- 다운로드 받은 zip중에서 opengl, glfw, macros, stb jar을 dependency설정
- Joml 1.8.2 : Matrix3D, Matrix4D -> 각각의 component를 rendering하기 위해 필요 -> https://github.com/JOML-CI/JOML/releases
  ● 1.8.2 버전 확인 필요!

2. internal files
- user.acc : 게임보드 자체를 저장하는 파일 -> load option을 사용하기 위해서는 user.acc file이 project repository에 필요
- rank.acc : 이전 플레이들의 랭킹을 저장하는 파일 -> 새로운 게임의 시작시에 자동으로 rank.acc에서 ranking을 load, 만약 없다면 새로운 ranking을 생성
- imgfolder : 게임의 rendering을 위해 필요한 모든 png파일이 위치. 기본적으로 git repository에 위치하나, snakeGame/imgfolder의 path를 유지해야 renderer에서 정상적으로 해당 png를 인식하므로 위치에 주의!

3. Game Play


1) 초기화면
- 메뉴의 클릭을 통해 실행
- play : snakeGame이 시작된다.
- load : user.acc에 가장 최근에 저장된 게임이 load된다.
- ranking : rank.acc에 저장된 local에서 플레이한 게임의 ranking이 표시된다
- exit : 게임 종료

2) 게임 시작
- 방향키 : snake의 direction을 변경시킨다
- esc & p : menu를 실행시킨다.

3) menu
- resume : 게임을 재개한다 -> 정지했던 그 시점의 게임이 재시작된다.
- restart : score, fruitPosition, snake를 포함한 모든 것을 초기상태로 되돌린 이후 게임을 시작한다
- save : 해당 게임 상태를 저장한다
- exit : mainMenu로 돌아간다.

Class Explanation
-
1. SnakeBody
- 게임에 돌아다니는 snake를 구성하는 cell하나
- snake의 위치정보를 가지고 있으며, rendering의 대상이 된다.
- positionX, positionY(float) : 해당 snakeBody의 coordinate
- method : changeDirection, move, getter, movePosition(specific position)

2. Snake
- 움직이는 snake 그 자체
- list of SnakeBody로 구성되어있으며, 메서드를 통해 SnakeBody를 제어한다.
- method : grow, change_Direction, move, check_If_Collapse, check_If_Overlap, re_Init

3. GameBoard
- SnakeGame의 진행동안 이루어져야 하는 모든 operation을 통제하는 class
- Snake의 통제, Fruit의 생성, score의 갱신, Ranking의 관리, save & load
- method : loadGame, createFruit, move_Snake, change_Direction_Snake, setNickname, check_fruit_overlap, save_this_game, loadGame

4. Controller
- gameBoard와 renderer, LWGJL window의 interaction을 중개해주는 class
- keyboard Event handler를 정의
- loop : 실질적인 operation
