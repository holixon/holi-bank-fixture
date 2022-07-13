# holi-bank-fixture

[![Build Status](https://github.com/holixon/holi-bank-fixture/workflows/Development%20branches/badge.svg)](https://github.com/holixon/holi-bank-fixture/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)

A CQRS/ES bank account example application. Used as test fixture in holixon libs.

## Example description

This application is a simple implementation of functionality from banking domain.

### Bank Account

A bank account is responsible for storing amount of money. It has a unique account id.
Money can be deposited to the account and withdrawn from the account. In the following the example use cases are
described:

### UC1: Create Account

To create a bank account you need to specify the account id, the initial balance and the maximum balance.
By default, the account id is auto-generated, the initial balance is 0 and the maximum balance 1000.

### UC2: Deposit Money

To deposit money on a bank account you need to specify the account id and
the amount. If the current balance exceeds the maximum balance, the deposit is not possible, otherwise the amount is added to the balance.

### UC3: Withdraw Money

To withdraw money from a bank account you need to specify the account id and
the amount. If the current balance is smaller than the amount you are requesting, the withdrawal is not possible,
otherwise the current balance is reduced by the withdrawal amount.

### UC4: Retrieving current balance

To retrieve the current account balance you need to specify the account id. As a result the current balance is returned.

### UC5: Money transfer

TODO!