package WayofTime.bloodmagic.util;

public class BloodMagicIsntReadyYetStopTryingToUseItAndJustWaitForMeToFinishPleaseAndThankYou extends Error {
    public BloodMagicIsntReadyYetStopTryingToUseItAndJustWaitForMeToFinishPleaseAndThankYou() {
        super("Blood Magic isn't ready yet. Stop trying to use it and just wait for me to finish. Please and thank you.");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
