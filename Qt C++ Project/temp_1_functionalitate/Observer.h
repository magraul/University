#pragma once
#include <vector>
#include <algorithm>

using std::vector;

class Observer {
public:
	//virtual void update() = 0;
};


class Observable {
private:
	//facem lista cu elementele care se inscriu ca oblervablie
	vector<Observer*> observers;
public:
	void add_observer(Observer* obs) {
		observers.push_back(obs);
	}

	void remove_observer(Observer* obs) {
		observers.erase(std::remove(observers.begin(), observers.end(), obs), observers.end());
	}

	void notify() {
		//for (auto obs : observers)
			//obs->update();
	}
};