#include <iostream>
#include "Test.h"
#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>
#include <stdlib.h>

using std::cout;

int main() {
	testAll();
	testAllExtins();
	_CrtDumpMemoryLeaks();
	cout << "Trecut teste!\n";
	system("pause");
	return 0;
}