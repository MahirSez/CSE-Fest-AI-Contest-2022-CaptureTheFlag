#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

int main()
{
    int height;
    int width;
    cin >> height >> width; cin.ignore();
    for (int i = 0; i < height; i++) {
        string row;
        getline(cin, row);
    }
    int my_flag_base_x;
    int my_flag_base_y;
    cin >> my_flag_base_x >> my_flag_base_y; cin.ignore();
    int opponent_flag_base_x;
    int opponent_flag_base_y;
    cin >> opponent_flag_base_x >> opponent_flag_base_y; cin.ignore();

    // game loop
    while (1) {
        int my_score;
        int opponent_score;
        cin >> my_score >> opponent_score; cin.ignore();
        int my_flag_pos_x;
        int my_flag_pos_y;
        bool my_flag_captured;
        cin >> my_flag_pos_x >> my_flag_pos_y >> my_flag_captured; cin.ignore();
        int opponent_flag_pos_x;
        int opponent_flag_pos_y;
        bool opponent_flag_captured;
        cin >> opponent_flag_pos_x >> opponent_flag_pos_y >> opponent_flag_captured; cin.ignore();
        int my_alive_minion_cnt;
        cin >> my_alive_minion_cnt; cin.ignore();
        for (int i = 0; i < my_alive_minion_cnt; i++) {
            int id;
            int pos_x;
            int pos_y;
            int health;
            cin >> id >> pos_x >> pos_y >> health; cin.ignore();
        }
        int visible_minion_cnt;
        cin >> visible_minion_cnt; cin.ignore();
        for (int i = 0; i < visible_minion_cnt; i++) {
            int id;
            int pos_x;
            int pos_y;
            int health;
            cin >> id >> pos_x >> pos_y >> health; cin.ignore();
        }
        for(int i = 0 ; i < my_alive_minion_cnt ; i++) {
            if(i) cout<<" | ";
            if(opponent_flag_captured) cout<<"MOVE "<<i<<" "<<my_flag_base_x<<" "<<my_flag_base_y;
            else cout<<"MOVE "<<i<<" "<<opponent_flag_base_x<<" "<<opponent_flag_base_y;
        }
        cout<<endl;
    }
}