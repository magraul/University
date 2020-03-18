/********************************************************************************
** Form generated from reading UI file 'temp_1_functionalitate.ui'
**
** Created by: Qt User Interface Compiler version 5.12.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_TEMP_1_FUNCTIONALITATE_H
#define UI_TEMP_1_FUNCTIONALITATE_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_temp_1_functionalitateClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *temp_1_functionalitateClass)
    {
        if (temp_1_functionalitateClass->objectName().isEmpty())
            temp_1_functionalitateClass->setObjectName(QString::fromUtf8("temp_1_functionalitateClass"));
        temp_1_functionalitateClass->resize(600, 400);
        menuBar = new QMenuBar(temp_1_functionalitateClass);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        temp_1_functionalitateClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(temp_1_functionalitateClass);
        mainToolBar->setObjectName(QString::fromUtf8("mainToolBar"));
        temp_1_functionalitateClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(temp_1_functionalitateClass);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        temp_1_functionalitateClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(temp_1_functionalitateClass);
        statusBar->setObjectName(QString::fromUtf8("statusBar"));
        temp_1_functionalitateClass->setStatusBar(statusBar);

        retranslateUi(temp_1_functionalitateClass);

        QMetaObject::connectSlotsByName(temp_1_functionalitateClass);
    } // setupUi

    void retranslateUi(QMainWindow *temp_1_functionalitateClass)
    {
        temp_1_functionalitateClass->setWindowTitle(QApplication::translate("temp_1_functionalitateClass", "temp_1_functionalitate", nullptr));
    } // retranslateUi

};

namespace Ui {
    class temp_1_functionalitateClass: public Ui_temp_1_functionalitateClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_TEMP_1_FUNCTIONALITATE_H
