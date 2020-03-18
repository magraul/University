#include <iostream>
#include "Dictionar.h"
#include "Iterator.h"
#include "TestScurt.h"
#include "TestExtins.h"
#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>

int main() {
	testAll();
	testAllExtins();
	_CrtDumpMemoryLeaks();
	printf("succes teste!\n");
	system("pause");
	return 0;
}