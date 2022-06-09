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

    string fireName;
    int firePrice, fireDamage;
    cin>>fireName>>firePrice>>fireDamage;

    string freezeName;
    int freezePrice, freezeDamage;
    cin>>freezeName>>freezePrice>>freezeDamage;

    string mineName;
    int minePrice, mineDamage;
    cin>>mineName>>minePrice>>mineDamage;


    // game loop
    while (1) {
        int my_score;
        int opponent_score;
        cin >> my_score >> opponent_score; cin.ignore();
        int my_flag_pos_x;
        int my_flag_pos_y;
        int my_flag_carrier;
        cin >> my_flag_pos_x >> my_flag_pos_y >> my_flag_carrier; cin.ignore();
        int opponent_flag_pos_x;
        int opponent_flag_pos_y;
        int opp_flag_carrier;
        cin >> opponent_flag_pos_x >> opponent_flag_pos_y >> opp_flag_carrier; cin.ignore();
        int my_alive_minion_cnt;
        cin >> my_alive_minion_cnt; cin.ignore();

        vector<int>ids;
        for (int i = 0; i < my_alive_minion_cnt; i++) {
            int id;
            int pos_x;
            int pos_y;
            int health;
            int timeout;
            cin >> id >> pos_x >> pos_y >> health >> timeout; cin.ignore();
            ids.push_back(id);
        }
        int visible_minion_cnt;
        cin >> visible_minion_cnt; cin.ignore();
        for (int i = 0; i < visible_minion_cnt; i++) {
            int id;
            int pos_x;
            int pos_y;
            int health;
            int timeout;
            cin >> id >> pos_x >> pos_y >> health >> timeout; cin.ignore();
        }
        int visible_coin;
        cin>>visible_coin;
        for(int i = 0; i < visible_coin; i++) {
            int x, y;
            cin>>x>>y;
        }

        for(int i = 0 ; i < my_alive_minion_cnt ; i++) {
            if(i > 0) cout<<" | ";

            if(visible_minion_cnt > 0 && my_score >= firePrice && my_flag_carrier != -1) {
                cout<<"FIRE "<<ids[i];
            }
            else if(opp_flag_carrier != -1) {
                cout<<"MOVE "<<ids[i]<<" "<<my_flag_base_x<<" "<<my_flag_base_y;
            }
            else {
                cout<<"MOVE "<<ids[i]<<" "<<opponent_flag_pos_x<<" "<<opponent_flag_pos_y;
            }
        }
        cout<<endl;
    }
}