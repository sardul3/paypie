FILE="build/reports/pitest/mutations.xml"
mkdir -p mutation-summary

if [[ -f "$FILE" ]]; then
  # Count only valid mutations with status attribute
  TOTAL=$(grep -c "<mutation " "$FILE")
  KILLED=$(grep "status='KILLED'" "$FILE" | wc -l)
  SURVIVED=$(grep "status='SURVIVED'" "$FILE" | wc -l)

  # Accurate mutation score
  if [ "$TOTAL" -gt 0 ]; then
    MUTATION_RATE=$(echo "scale=2; ($KILLED / $TOTAL) * 100" | bc)
  else
    MUTATION_RATE="0.00"
  fi

  {
    echo "### PIT Mutation Summary"
    echo ""
    echo "- ✅ Total Mutants: **$TOTAL**"
    echo "- 🧪 Killed: **$KILLED**"
    echo "- ⚠️ Survived: **$SURVIVED**"
    echo "- 🎯 Mutation Score: **$MUTATION_RATE%**"
  } > mutation-summary/summary.md

  echo "✅ Mutation summary written to mutation-summary/summary.md"
else
  echo "⚠️ No mutation-report.xml found"
  echo "No mutation-report.xml found" > mutation-summary/summary.md
fi
