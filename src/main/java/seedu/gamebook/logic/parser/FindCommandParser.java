package seedu.gamebook.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.gamebook.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.gamebook.logic.commands.FindCommand;
import seedu.gamebook.logic.parser.exceptions.ParseException;
import seedu.gamebook.model.gameentry.GameEntryContainsKeywordPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindCommand(new GameEntryContainsKeywordPredicate(Arrays.asList(nameKeywords)));
    }

}