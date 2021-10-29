package seedu.gamebook.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.gamebook.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.gamebook.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.gamebook.testutil.Assert.assertThrows;
import static seedu.gamebook.testutil.TypicalIndexes.INDEX_FIRST_GAMEENTRY;

import org.junit.jupiter.api.Test;

import seedu.gamebook.logic.commands.AddCommand;
import seedu.gamebook.logic.commands.ClearCommand;
import seedu.gamebook.logic.commands.DeleteCommand;
import seedu.gamebook.logic.commands.EditCommand;
import seedu.gamebook.logic.commands.EditCommand.EditGameEntryDescriptor;
import seedu.gamebook.logic.commands.ExitCommand;
import seedu.gamebook.logic.commands.HelpCommand;
import seedu.gamebook.logic.commands.ListCommand;
import seedu.gamebook.logic.parser.exceptions.ParseException;
import seedu.gamebook.model.gameentry.GameEntry;
import seedu.gamebook.testutil.EditGameEntryDescriptorBuilder;
import seedu.gamebook.testutil.GameEntryBuilder;
import seedu.gamebook.testutil.GameEntryUtil;

public class GameBookParserTest {

    private final GameBookParser parser = new GameBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        GameEntry gameEntry = new GameEntryBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(GameEntryUtil.getAddCommand(gameEntry));
        assertEquals(new AddCommand(gameEntry), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_GAMEENTRY.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_GAMEENTRY), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        GameEntry gameEntry = new GameEntryBuilder().build();
        EditGameEntryDescriptor descriptor = new EditGameEntryDescriptorBuilder(gameEntry).build();
        String userInput = EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_GAMEENTRY.getOneBased() + " "
                + GameEntryUtil.getEditGameEntryDescriptorDetails(descriptor);
        EditCommand command = (EditCommand) parser.parseCommand(userInput);
        assertEquals(new EditCommand(INDEX_FIRST_GAMEENTRY, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }


    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.INVALID_COMMAND_MESSAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}