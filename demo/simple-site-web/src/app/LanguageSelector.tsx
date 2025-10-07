import { WebComponentWrapper } from '@/common/wrapper';
import { LanguageSelectorSkeleton } from '@g/app/LanguageSelectorSkeleton';
import { initStateSetters } from '@/common/component';

function LanguageSelectorFC(props: { element: LanguageSelectorComponent }) {
    initStateSetters(props.element);
    const value = !!props.element.getMultiple()
        ? props.element.getValue()?.values || []
        : props.element.getValue()?.values?.[0];
    return (
        <WebComponentWrapper element={props.element}>
            <select
                key="select"
                className="webpeer-select"
                multiple={!!props.element.getMultiple()}
                value={value}
                onChange={(e) => {
                    const values = [...e.target.options]
                        .filter((it) => it.selected)
                        .map((it) => it.value);
                    props.element.setValue({
                        values,
                    });
                }}
            >
                {props.element.getOptions().map((it) => (
                    <option key={it.id} value={it.id}>
                        {it.displayName}
                    </option>
                ))}
            </select>
        </WebComponentWrapper>
    );
}

export class LanguageSelectorComponent extends LanguageSelectorSkeleton {
    functionalComponent = LanguageSelectorFC;
}
