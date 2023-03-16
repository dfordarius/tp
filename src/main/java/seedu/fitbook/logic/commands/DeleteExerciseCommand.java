package seedu.fitbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.fitbook.commons.core.Messages;
import seedu.fitbook.commons.core.index.Index;
import seedu.fitbook.logic.commands.exceptions.CommandException;
import seedu.fitbook.model.FitBookModel;
import seedu.fitbook.model.routines.Exercise;
import seedu.fitbook.model.routines.Routine;

/**
 * Deletes a Routine identified using it's displayed index from the FitBook.
 */
public class DeleteExerciseCommand extends Command {

    public static final String COMMAND_WORD = "deleteExercise";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the exercise identified by the index number of the routine identified by the index number.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1" + " 2";

    public static final String MESSAGE_DELETE_ROUTINE_SUCCESS = "Deleted Exercise: %1$s from Routine: %2$s";

    private final Index targetRoutine;
    private final Index targetExercise;

    public DeleteExerciseCommand(Index targetRoutine, Index targetExercise) {
        this.targetRoutine = targetRoutine;
        this.targetExercise = targetExercise;
    }

    @Override
    public CommandResult execute(FitBookModel model) throws CommandException {
        requireNonNull(model);
        List<Routine> lastShownList = model.getFilteredRoutineList();

        if (targetRoutine.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ROUTINE_DISPLAYED_INDEX);
        }
        Routine routine = lastShownList.get(targetRoutine.getZeroBased());
        if (targetExercise.getZeroBased() >= routine.getExercises().size()) {
            throw new CommandException("Exercise index invalid");
        }
        Routine routineToDelete = lastShownList.get(targetRoutine.getZeroBased());
        Exercise exercise1 = routine.getExercises().get(targetExercise.getZeroBased());
        System.out.println(routineToDelete.getExercises().size());
        for(Exercise exercise : routine.getExercises()) {
            System.out.println(exercise.exerciseName.toString());
        }
        model.removeExercise(routineToDelete, targetExercise.getZeroBased());
        System.out.println(routineToDelete.getExercises().size());
        return new CommandResult(String.format(MESSAGE_DELETE_ROUTINE_SUCCESS, exercise1, routineToDelete));
    }
/*
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteRoutineCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteRoutineCommand) other).targetIndex)); // state check
    }

 */
}