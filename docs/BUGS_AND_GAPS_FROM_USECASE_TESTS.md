# Bugs / Missing Edge Cases / Validation (from use-case-driven unit tests)

This list is derived from **failing** use-case-level unit tests added under each use case’s test class in `Edge cases and validation` nested classes. The tests use `@DisplayName` to state expected behaviour; they do not assert on implementation details. Fixes to the main code are planned for the next phase.

---

## 1. CreateExpenseActivityService

| # | Test (DisplayName) | Gap / Bug |
|---|--------------------|-----------|
| 1.1 | When group does not exist, throw ExpenseGroupNotFoundException **with group id in message** | **Exception message does not include group id.** Service throws `"Expense Group not found"`. Expected: message contains the group id (e.g. `"Expense group not found: <groupId>"`) for better diagnostics. |
| 1.2 | When **splitWith contains participant ID not in group**, throw clear exception | **No validation of split membership.** If `splitWith` contains a participant ID that is not in the group, the use case/domain currently does not throw; non-members are effectively ignored when resolving split. Expected: throw `IllegalArgumentException` (or domain equivalent) when any `splitWith` ID is not a member of the group. |

---

## 2. SettleUpService

| # | Test (DisplayName) | Gap / Bug |
|---|--------------------|-----------|
| 2.1 | When **groupId is null**, throw IllegalArgumentException or NullPointerException with clear contract | **Null groupId not validated.** Repository is called with `null`; if it returns empty, the service throws `ExpenseGroupNotFoundException("Expense group not found: null")`. Expected: validate up front and throw `IllegalArgumentException` for null groupId so invalid argument is distinguished from “group not found”. |
| 2.2 | When **command is null**, throw rather than NPE deep in call stack | **Null command not validated.** If the group is found, the service proceeds to use `command.fromParticipantId()` etc. and will throw NPE. If the group is not found, callers get `ExpenseGroupNotFoundException` before touching the command. Expected: validate command non-null at entry and throw `IllegalArgumentException`. |

---

## 3. GetExpenseHistoryService

| # | Test (DisplayName) | Gap / Bug |
|---|--------------------|-----------|
| 3.1 | When **page is negative**, throw IllegalArgumentException | **No validation of `page`.** Negative page is passed through to the query repository. Expected: reject with `IllegalArgumentException` and message containing "page". |
| 3.2 | When **size is zero**, throw IllegalArgumentException | **No validation of `size`.** Zero page size is allowed. Expected: reject with `IllegalArgumentException` and message containing "size". |
| 3.3 | When **size is negative**, throw IllegalArgumentException | **No validation of `size`.** Negative size is passed through. Expected: reject with `IllegalArgumentException`. |
| 3.4 | When **size exceeds maximum allowed**, throw IllegalArgumentException | **No maximum page size.** Very large `size` (e.g. 10_000) is accepted. Expected: define a max page size (e.g. in config), validate, and throw `IllegalArgumentException` with message containing "size" when exceeded. |
| 3.5 | When **groupId is null**, throw rather than proceed | **Null groupId not validated.** Same as 2.1: repository returns empty, service throws `ExpenseGroupNotFoundException`. Expected: validate and throw `IllegalArgumentException` for null groupId. |

---

## 4. AddParticipantService

All added edge-case tests **passed** (null/blank email and null groupId/command are either validated or result in an accepted exception type). No new bugs or gaps listed from these tests.

---

## 5. RetrieveExpenseGroupService

| # | Test (DisplayName) | Gap / Bug |
|---|--------------------|-----------|
| 5.1 | When **groupId is null**, throw IllegalArgumentException or NPE rather than proceed | **Null groupId not validated.** Same as 2.1/3.5: leads to `ExpenseGroupNotFoundException`. Expected: validate and throw `IllegalArgumentException` for null groupId. |

---

## 6. GetGroupBalanceService

| # | Test (DisplayName) | Gap / Bug |
|---|--------------------|-----------|
| 6.1 | When **groupId is null**, throw IllegalArgumentException or NPE rather than proceed | **Null groupId not validated.** Same as 2.1/3.5/5.1. Expected: validate and throw `IllegalArgumentException` for null groupId. |

---

## Summary

- **Null groupId:** Multiple use cases (SettleUp, GetExpenseHistory, RetrieveExpenseGroup, GetGroupBalance) do not validate null groupId; they call the repository with null, get empty, and throw `ExpenseGroupNotFoundException`. Preferred: validate at use-case entry and throw `IllegalArgumentException` for invalid argument.
- **Null command (SettleUp):** Command is not checked for null; can lead to NPE if the group exists.
- **GetExpenseHistory pagination:** No validation of `page` (e.g. negative) or `size` (zero, negative, or above a maximum).
- **CreateExpenseActivity:** (1) Exception message when group not found should include group id; (2) `splitWith` should be validated so that every ID is a member of the group, otherwise throw a clear exception.

These findings can be used to implement validation and error messages in the application layer (and, where appropriate, in the domain) in the next phase.
