# PayPie Core Domain User Stories

## Epic 1: Group Management

### User Story 1.1: Create Expense Group
**As a** user  
**I want to** create an expense group  
**So that** I can start tracking shared expenses with my friends

#### Acceptance Criteria
1. Group must have a unique name
2. Group must have at least 2 participants
3. Each participant must have a valid email
4. Initial group balance should be zero
5. Creator should be automatically added as a participant

#### TDD Tasks

1. **Value Object: GroupName**
```java
// Test Cases
void shouldNotCreateEmptyGroupName()
void shouldNotCreateGroupNameExceeding50Chars()
void shouldNormalizeGroupNameSpaces()
void shouldCreateValidGroupName()

// Implementation Steps
1. Create GroupName class with validation
2. Implement value object equality
3. Add normalization logic
```

2. **Value Object: ParticipantEmail**
```java
// Test Cases
void shouldNotCreateInvalidEmailFormat()
void shouldNormalizeEmailToLowerCase()
void shouldCreateValidEmail()
void shouldImplementValueObjectEquality()

// Implementation Steps
1. Create ParticipantEmail class
2. Add email validation regex
3. Implement normalization
4. Add equality methods
```

3. **Entity: Participant**
```java
// Test Cases
void shouldCreateParticipantWithValidEmail()
void shouldNotCreateDuplicateParticipant()
void shouldInitializeWithZeroBalance()
void shouldUpdateParticipantBalance()

// Implementation Steps
1. Create Participant entity
2. Add balance tracking
3. Implement equality based on email [NA for entity, will do in AR]
4. Add balance update methods
```

4. **Aggregate Root: Group**
```java
// Test Cases
void shouldCreateGroupWithValidName()
void shouldRequireMinimumTwoParticipants()
void shouldNotAllowDuplicateParticipants()
void shouldAddCreatorAsParticipant()
void shouldInitializeWithZeroBalance()

// Implementation Steps
1. Create Group aggregate
2. Add participant management
3. Implement balance tracking
4. Add business rules validation
```

### User Story 1.2: Add Participants to Group
**As a** group member  
**I want to** add new participants to the group  
**So that** more people can share expenses

#### Acceptance Criteria
1. Only existing group members can add new participants
2. Cannot add duplicate participants
3. New participants start with zero balance
4. All existing members should be notified

#### TDD Tasks

1. **Domain Service: ParticipantService**
```java
// Test Cases
void shouldAddNewParticipantToGroup()
void shouldNotAddDuplicateParticipant()
void shouldNotifyExistingMembers()
void shouldInitializeWithZeroBalance()

// Implementation Steps
1. Create ParticipantService
2. Add validation logic
3. Implement notification
4. Add balance initialization
```

2. **Event: ParticipantAddedEvent**
```java
// Test Cases
void shouldCreateEventWithParticipantDetails()
void shouldIncludeTimestamp()
void shouldIncludeGroupContext()

// Implementation Steps
1. Create event class
2. Add required properties
3. Implement event handling
```

### User Story 1.3: View Group Balance
**As a** group member  
**I want to** view the current balance for all participants  
**So that** I know who owes whom

#### TDD Tasks

1. **Value Object: Balance**
```java
// Test Cases
void shouldCalculateNetBalance()
void shouldHandleMultipleCurrencies()
void shouldPreventNegativeAmount()
void shouldFormatBalanceWithCurrency()

// Implementation Steps
1. Create Balance class
2. Add currency support
3. Implement calculations
4. Add formatting
```

2. **Domain Service: BalanceCalculator**
```java
// Test Cases
void shouldCalculateIndividualBalances()
void shouldEnsureTotalBalanceIsZero()
void shouldHandleMultipleExpenses()
void shouldOptimizeSettlements()

// Implementation Steps
1. Create calculator service
2. Add balance algorithms
3. Implement optimization
4. Add validation
```

## Epic 2: Expense Management

### User Story 2.1: Add Expense
**As a** group member  
**I want to** add a new expense  
**So that** I can track money spent on behalf of the group

#### Acceptance Criteria
1. Expense must have an amount and description
2. Payer must be a group member
3. Split must be specified for all participants
4. Total split must equal expense amount
5. System should update all balances

#### TDD Tasks

1. **Value Object: Money**
```java
// Test Cases
void shouldNotCreateNegativeAmount()
void shouldValidateCurrencyCode()
void shouldPerformBasicArithmetic()
void shouldHandleRounding()

// Implementation Steps
1. Create Money class
2. Add currency validation
3. Implement arithmetic
4. Add rounding rules
```

2. **Value Object: ExpenseSplit**
```java
// Test Cases
void shouldValidateSplitPercentages()
void shouldEnsureTotalIs100Percent()
void shouldCalculateAmountPerParticipant()
void shouldHandleEqualSplits()

// Implementation Steps
1. Create split calculations
2. Add validation rules
3. Implement split types
4. Add helper methods
```

3. **Aggregate Root: Expense**
```java
// Test Cases
void shouldCreateValidExpense()
void shouldValidatePayerIsMember()
void shouldValidateSplitTotal()
void shouldUpdateGroupBalances()
void shouldGenerateExpenseEvent()

// Implementation Steps
1. Create Expense entity
2. Add validation rules
3. Implement balance updates
4. Add event generation
```

### User Story 2.2: Settle Up
**As a** group member  
**I want to** settle my debts with another member  
**So that** we can clear our balances

#### TDD Tasks

1. **Value Object: Settlement**
```java
// Test Cases
void shouldCreateValidSettlement()
void shouldValidateParticipants()
void shouldUpdateBalances()
void shouldRecordTimestamp()

// Implementation Steps
1. Create Settlement class
2. Add validation rules
3. Implement balance updates
4. Add audit fields
```

2. **Domain Service: SettlementService**
```java
// Test Cases
void shouldProcessValidSettlement()
void shouldPreventOverpayment()
void shouldUpdateBothParticipants()
void shouldGenerateSettlementEvent()

// Implementation Steps
1. Create service class
2. Add business rules
3. Implement event handling
4. Add validation
```

## Implementation Order

1. Start with Value Objects
   - GroupName
   - ParticipantEmail
   - Money
   - Balance

2. Move to Entities
   - Participant
   - Group (Aggregate Root)
   - Expense (Aggregate Root)

3. Implement Domain Services
   - ParticipantService
   - BalanceCalculator
   - SettlementService

4. Add Event Handling
   - ParticipantAddedEvent
   - ExpenseCreatedEvent
   - SettlementCompletedEvent

## Testing Guidelines

1. **Unit Tests**
   - Test each value object in isolation
   - Mock dependencies for entities
   - Use test data builders
   - Follow AAA pattern (Arrange-Act-Assert)

2. **Integration Tests**
   - Test aggregate roots with repositories
   - Verify event handling
   - Test service interactions

3. **End-to-End Tests**
   - Complete user workflows
   - Cross-cutting concerns
   - Performance scenarios 
