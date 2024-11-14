#include <bits/stdc++.h>

using namespace std;

int main() {
    int row, col;
    int my_base_x, my_base_y;
    int opp_base_x, opp_base_y;

    cin >> row >> col;
    for(int i = 0; i < row; i++) {
        string grid_row;
        cin >> grid_row;
    }

    cin >> my_base_x >> my_base_y;
    cin >> opp_base_x >> opp_base_y;

    for(int i = 0; i < 3; i++) {
        string power_up_name;
        int power_up_price;
        int power_up_damage;
        cin >> power_up_name >> power_up_price >> power_up_damage;
    }

    while(true) {
        int my_score, opp_score;
        int my_flag_x, my_flag_y, my_carrier;
        int opp_flag_x, opp_flag_y, opp_carrier;
        int alive_cnt, opp_seen_cnt;
        int visible_coin_cnt;

        cin >> my_score;
        cin >> opp_score;

        cin >> my_flag_x >> my_flag_y >> my_carrier;
        cin >> opp_flag_x >> opp_flag_y >> opp_carrier;

        cin >> alive_cnt;
        for(int i = 0; i < alive_cnt; i++) {
            int id, x, y, health, timeout;
            cin >> id >> x >> y >> health >> timeout;
        }

        cin >> opp_seen_cnt;
        for(int i = 0; i < opp_seen_cnt; i++) {
            int id, x, y, health, timeout;
            cin >> id >> x >> y >> health >> timeout;
        }

        cin >> visible_coin_cnt;
        for(int i = 0; i < visible_coin_cnt; i++) {
            int x, y;
            cin >> x >> y;
        }

        /// Your code goes here
        cout << "WAIT 0 | WAIT 1 | WAIT 2" << endl;
    }

    return 0;
}